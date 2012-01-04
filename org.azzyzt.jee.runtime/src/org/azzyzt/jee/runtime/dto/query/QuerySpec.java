/*
 * Copyright (c) 2011, Municipiality of Vienna, Austria
 *
 * Licensed under the EUPL, Version 1.1 or � as soon they
 * will be approved by the European Commission - subsequent
 * versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the
 * Licence.
 * 
 * For convenience a plain text copy of the English version 
 * of the Licence can be found in the file LICENCE.txt in
 * the top-level directory of this software distribution.
 * 
 * You may obtain a copy of the Licence in any of 22 European
 * Languages at:
 *
 * http://www.osor.eu/eupl
 *
 * Unless required by applicable law or agreed to in
 * writing, software distributed under the Licence is
 * distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the Licence for the specific language governing
 * permissions and limitations under the Licence.
 */

package org.azzyzt.jee.runtime.dto.query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.azzyzt.jee.runtime.exception.QuerySyntaxException;
import org.azzyzt.jee.runtime.util.AttributedTags2QuerySpec;
import org.azzyzt.jee.runtime.util.Xml2QuerySpec;

/**
 * <p>For each entity class Azzyzt JEE Tools create two type-specific service beans
 * with, among others, a <code>list</code> operation. List takes a <code>QuerySpec</code>
 * as parameter and delivers a list of DTOs, according to the result of the query.</p> 
 * 
 * <p>A query specification contains an expression that selects what objects are in the result,
 * and a list of <code>OrderByClause</code> that determines the order ther result list. 
 * Both are optional. The result of the empty query specification is a list of all objects
 * in a database table in unspecified order.</p>
 * 
 * <p>Query specifications can either be constructed as a tree of objects or as XML text.
 * The latter is the only way to submit a query via the generated REST interface.</p>
 * 
 * <p><code>QuerySpec</code> implements <code>Node</code>, because this way the parser can 
 * directly use the <code>QuerySpec</code> as root of the expression tree. 
 * 
 * @see Expression
 * @see Node
 * @see AttributedTags2QuerySpec
 * @see OrderByClause
 */
@XmlRootElement(name="query_spec")
public class QuerySpec implements Node, Serializable {
	
	private static final long serialVersionUID = 1L;

	private Expression expression;
	
	private List<OrderByClause> orderByList = new ArrayList<OrderByClause>();
	
	public QuerySpec() { }
	
	/**
	 * Programmatically construct a query specification from an expression and a 
	 * list of order-by clauses
	 * @param e an expression
	 * @param orderByList a list of order-by clauses
	 */
	public QuerySpec(Expression e, List<OrderByClause> orderByList) {
		this.expression = e;
		this.orderByList = orderByList;
	}
	
	/**
	 * Factory method that constructs a query specification from an XML representation.
	 * This is mandatory for REST clients, but may also be useful in cases where we want
	 * to read queries from files, etc. 
	 * @param querySpecXml an XML representation as it is used by REST clients
	 * @return the query specification as object tree
	 * @throws QuerySyntaxException
	 */
	public static QuerySpec fromXML(String querySpecXml) 
		throws QuerySyntaxException 
	{
		Xml2QuerySpec r2qs = new AttributedTags2QuerySpec();
		return r2qs.fromXML(querySpecXml);
	}

	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Node#add(org.azzyzt.jee.runtime.dto.query.Expression)
	 */
	@Override
	public void add(Expression expression) 
		throws QuerySyntaxException 
	{
		if (this.expression != null) {
			throw new QuerySyntaxException("A QuerySpec can only have a single expression");
		}
		this.expression = expression;
	}

	public List<OrderByClause> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<OrderByClause> orderByList) {
		this.orderByList = orderByList;
	}

	public void addOrderBy(OrderByClause orderBy) {
		this.orderByList.add(orderBy);
	}

	@Override
	public String toString() {
		return "QuerySpec [expression=" + expression + ", orderBy=" + orderByList
		+ "]";
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Node#getReplaceableBy()
	 */
	@Override
	public Expression getReplaceableBy() {
		// makes no sense at top node, but is enforced by Node interface
		return null;
	}

	/* (non-Javadoc)
	 * @see org.azzyzt.jee.runtime.dto.query.Node#isValid()
	 */
	@Override
	public boolean isValid() {
		// may make sense at top node, is enforced by Node interface
		return false;
	}

}
