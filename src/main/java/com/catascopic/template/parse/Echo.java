package com.catascopic.template.parse;

import java.io.IOException;

import com.catascopic.template.Scope;
import com.catascopic.template.expr.Term;
import com.catascopic.template.expr.Values;

class Echo implements Node {

	private final Term term;

	Echo(Term term) {
		this.term = term;
	}

	@Override
	public void render(Appendable writer, Scope scope) throws IOException {
		writer.append(Values.toString(term.evaluate(scope)));
	}

}