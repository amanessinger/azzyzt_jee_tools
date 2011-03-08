package org.azzyzt.jee.runtime.util;

import java.io.StringReader;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.DefaultHandler;

import org.azzyzt.jee.runtime.dto.query.And;
import org.azzyzt.jee.runtime.dto.query.BinaryBooleanExpression;
import org.azzyzt.jee.runtime.dto.query.BinaryFieldExpression;
import org.azzyzt.jee.runtime.dto.query.BinaryFieldOperator;
import org.azzyzt.jee.runtime.dto.query.FieldExpression;
import org.azzyzt.jee.runtime.dto.query.FieldReference;
import org.azzyzt.jee.runtime.dto.query.FieldReferer;
import org.azzyzt.jee.runtime.dto.query.Literal;
import org.azzyzt.jee.runtime.dto.query.LiteralType;
import org.azzyzt.jee.runtime.dto.query.Node;
import org.azzyzt.jee.runtime.dto.query.Not;
import org.azzyzt.jee.runtime.dto.query.Or;
import org.azzyzt.jee.runtime.dto.query.OrderByClause;
import org.azzyzt.jee.runtime.dto.query.QuerySpec;
import org.azzyzt.jee.runtime.dto.query.UnaryFieldExpression;
import org.azzyzt.jee.runtime.dto.query.UnaryFieldOperator;
import org.azzyzt.jee.runtime.exception.NotYetImplementedException;
import org.azzyzt.jee.runtime.exception.QuerySyntaxException;

public class AttributedTags2QuerySpec extends DefaultHandler implements REST2QuerySpec {

	private static final SAXParserFactory _SAX_PARSER_FACTORY;
	static {
		_SAX_PARSER_FACTORY = SAXParserFactory.newInstance();
		try {
			_SAX_PARSER_FACTORY.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		} catch (SAXNotRecognizedException e) {
			// ignored
		} catch (SAXNotSupportedException e) {
			// ignored
		} catch (ParserConfigurationException e) {
			// ignored
		}
	}

	private boolean collect;

	private QuerySpec result;
	private OrderByClause currentOrderBy;
	private Stack<Node> currentNode;
	private FieldExpression currentFieldExpression;
	private LiteralType currentFieldExpressionType;
	private FieldReferer currentFieldReferer;
	private String collected;

	public AttributedTags2QuerySpec() { }
	
	/* (non-Javadoc)
	 * @see com.manessinger.util.jee.util.REST2QuerySpec#fromXML(java.lang.String)
	 */
	public QuerySpec fromXML(String xml) throws QuerySyntaxException {
		try {
			SAXParser saxParser = _SAX_PARSER_FACTORY.newSAXParser();
			saxParser.parse(new InputSource(new StringReader(xml)), this);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new QuerySyntaxException(
					"Query "+(xml != null ? xml : "<null>")+" may be invalid or parsing failed for any other reason", 
					e
			);
		}
	}

	@Override
	public void startDocument() 
		throws SAXException 
	{
		result = new QuerySpec();
		currentNode = new Stack<Node>();
		currentNode.push(result);
		currentFieldExpression = null;
		currentFieldReferer = null;
		currentOrderBy = null;
		collect = false;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
		throws SAXException 
	{
		try {
			String type = attributes.getValue("type");
			String negated = attributes.getValue("negated");
			boolean isNegated = false;
			if (negated != null) {
				if (negated.equalsIgnoreCase("true")) {
					isNegated = true;
				}
			}
			String caseSensitive = attributes.getValue("caseSensitive");
			boolean isCaseSensitive = false;
			if (caseSensitive != null) {
				if (caseSensitive.equalsIgnoreCase("true")) {
					isCaseSensitive = true;
				}
			}
			
			if (qName.equalsIgnoreCase("query_spec")) {
				// nothing to do
			} else if (qName.equalsIgnoreCase("expr") && type != null && type.equalsIgnoreCase("and")) {
				And a = new And();
				currentNode.peek().add(a);
				currentNode.push(a);
			} else if (qName.equalsIgnoreCase("expr") && type != null && type.equalsIgnoreCase("or")) {
				BinaryBooleanExpression o = new Or();
				currentNode.peek().add(o);
				currentNode.push(o);
			} else if (qName.equalsIgnoreCase("expr") && type != null && type.equalsIgnoreCase("not")) {
				Not n = new Not();
				currentNode.peek().add(n);
				currentNode.push(n);
			} else if (qName.equalsIgnoreCase("cond")) {
				String op = attributes.getValue("op");
				try {
					BinaryFieldOperator binOp = BinaryFieldOperator.valueOf(op);
					BinaryFieldExpression bfe = new BinaryFieldExpression();
					bfe.setOp(binOp);
					bfe.setCaseSensitive(isCaseSensitive);
					currentFieldExpression = bfe;
					if (type == null) {
						throw new QuerySyntaxException("No datatype given for binary operation "+binOp.name());
					}
					try {
						currentFieldExpressionType = LiteralType.valueOf(type.toUpperCase());
					} catch (IllegalArgumentException ex) {
						throw new QuerySyntaxException("Unsupported datatype "+type);
					}
				} catch (IllegalArgumentException ex) {
					try {
						UnaryFieldOperator unOp = UnaryFieldOperator.valueOf(op);
						UnaryFieldExpression ufe = new UnaryFieldExpression();
						ufe.setOp(unOp);
						currentFieldExpression = ufe;
					} catch (IllegalArgumentException ex2) {
						throw new QuerySyntaxException("Unsupported operation "+op);
					}
				}
				currentFieldExpression.setNegated(isNegated);
				currentFieldReferer = currentFieldExpression;
			} else if (qName.equalsIgnoreCase("orderBy")) {
				currentOrderBy = new OrderByClause();
				currentFieldReferer = currentOrderBy;
				currentFieldExpression = null;
			} else if (qName.equalsIgnoreCase("fieldName") 
					|| qName.equalsIgnoreCase("value") 
					|| qName.equalsIgnoreCase("ascending")) {
				collect = true;
			} else {
				throw new NotYetImplementedException();
			}
		} catch (Exception e) {
			throw new SAXException(e);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
		throws SAXException 
	{
		try {
			if (qName.equalsIgnoreCase("query_spec")) {
				// nothing to do
			} else if (qName.equalsIgnoreCase("cond")) {
				currentNode.peek().add(currentFieldExpression);
				currentFieldExpression = null;
			} else if (qName.equalsIgnoreCase("expr")) {
				currentNode.pop();
				currentFieldExpression = null;
			} else if (qName.equalsIgnoreCase("fieldName")) {
				currentFieldReferer.setFieldName(getCollected());
				collect = false;
			} else if (qName.equalsIgnoreCase("fieldName2")) {
				BinaryFieldExpression bfe = currentBinaryFieldExpression();
				bfe.setOperand(new FieldReference(getCollected(), bfe.isCaseSensitive()));
				collect = false;
			} else if (qName.equalsIgnoreCase("value")) {
				BinaryFieldExpression bfe = currentBinaryFieldExpression();
				Literal value = Literal.parse(getCollected(), currentFieldExpressionType);
				bfe.setOperand(value);
				collect = false;
			} else if (qName.equalsIgnoreCase("ascending")) {
				currentOrderBy.setAscending(Boolean.parseBoolean(collected));
			} else if (qName.equalsIgnoreCase("orderBy")) {
				result.addOrderBy(currentOrderBy);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new SAXException(e);
		}
	}

	private BinaryFieldExpression currentBinaryFieldExpression()
			throws QuerySyntaxException {
		if ( ! (currentFieldReferer instanceof BinaryFieldExpression 
					&& currentFieldExpression.equals(currentFieldReferer)
				)
		) {
			throw new QuerySyntaxException("BinaryFieldExpression syntax not inside a BinaryFieldExpression");
		}
		BinaryFieldExpression bfe = (BinaryFieldExpression)currentFieldExpression;
		return bfe;
	}

	private String getCollected() {
		String result = collected;
		collected = null;
		return result;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
		throws SAXException 
	{
		if (!collect) return;
		
		collected = new String(ch, start, length);
		collected = collected.trim();
	}
}

