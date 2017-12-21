package au.com.codeka.carrot.expr;

import java.util.Set;

import com.google.common.collect.Sets;

import au.com.codeka.carrot.CarrotException;

/**
 * A factory for binary {@link Term}s.
 *
 * @author Marten Gajda
 */
final class BinaryTermParser implements TermParser {

	private final TermParser termParser;
	private final Set<TokenType> tokenTypes;

	BinaryTermParser(TermParser termParser, TokenType first, TokenType... rest) {
		this.termParser = termParser;
		this.tokenTypes = Sets.immutableEnumSet(first, rest);
	}

	@Override
	public Term parse(Tokenizer tokenizer) throws CarrotException {
		Term left = termParser.parse(tokenizer);
		while (tokenizer.check(tokenTypes)) {
			left = new BinaryTerm(left,
					tokenizer.next().getType().binaryOperator(),
					termParser.parse(tokenizer));
		}
		return left;
	}

}