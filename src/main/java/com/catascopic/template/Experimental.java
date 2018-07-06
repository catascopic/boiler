package com.catascopic.template;

import java.io.IOException;
import java.io.PushbackReader;

public class Experimental {

	private static final int RADIX = 10;

	public static Number readNumber(PushbackReader reader) throws IOException {
		int integer = 0;
		boolean hasDigit;
		loop: for (;;) {
			int ch = reader.read();
			int digit = Character.digit(ch, RADIX);
			if (digit == -1) {
				switch (ch) {
				case '.':
					return readDecimal(reader, integer, hasDigit);
				case 'e':
				case 'E':
					return hasDigit ? readScientific(reader, integer, 0.0) : null;
				default:
					reader.unread(ch);
				case -1:
					return hasDigit ? integer : null;
				}
			} else {
				integer *= RADIX;
				integer += digit;
				hasDigit = true;
			}
		}
	}

	private static Number readDecimal(PushbackReader reader, int integer,
			boolean hasDigitSoFar) {
		double decimal = 0;
		boolean hasDigit = hasDigitSoFar;
		loop: for (;;) {
			int ch = reader.read();
			int digit = Character.digit(ch, RADIX);
			if (digit == -1) {
				switch (ch) {
				case 'e':
				case 'E':
					return readScientific(reader, integer, decimal);
				default:
					reader.unread(ch);
				case -1:
					return hasDigit ? integer : null;
				}
			} else {
				integer /= RADIX;
				integer += digit;
				hasDigit = true;
			}
		}
	}

}