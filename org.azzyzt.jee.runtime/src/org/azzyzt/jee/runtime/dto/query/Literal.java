package org.azzyzt.jee.runtime.dto.query;

import org.azzyzt.jee.runtime.exception.NotYetImplementedException;

public class Literal extends Operand {
	
	public Literal() { }
	
	public Literal(LiteralType type, Object value) {
		super();
		this.type = type;
		this.value = value;
	}

	private LiteralType type;
	
	private Object value;

	public LiteralType getType() {
		return type;
	}

	public void setType(LiteralType type) {
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public static Literal parse(String text, LiteralType type) 
		throws NotYetImplementedException 
	{
		Literal l = new Literal();
		l.setType(type);
		
		if (text != null) {
			switch (type) {
			case STRING:
				l.setValue(text.trim());
				break;
			case SHORT:
				l.setValue(Short.parseShort(text));
				break;
			case INTEGER:
				l.setValue(Integer.parseInt(text));
				break;
			case LONG:
				l.setValue(Long.parseLong(text));
				break;
			case FLOAT:
				l.setValue(Float.parseFloat(text));
				break;
			case DOUBLE:
				l.setValue(Double.parseDouble(text));
				break;
			default:
				throw new NotYetImplementedException();
			}
		}
		return l;
	}

	@Override
	public boolean isValid() {
		if (type == null || value == null)
			return false;
		if (!type.clazz().isAssignableFrom(value.getClass()))
			return false;
		return true;
	}

}
