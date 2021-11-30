package northwind.util;

import com.diogonunes.jcdp.color.ColoredPrinter;
import com.diogonunes.jcdp.color.api.Ansi.Attribute;
import com.diogonunes.jcdp.color.api.Ansi.BColor;
import com.diogonunes.jcdp.color.api.Ansi.FColor;

public class ColorConsolePrinter {

	private static final ColoredPrinter cp ;
	static {
		 cp = new ColoredPrinter.Builder(1, false)
                .foreground(FColor.WHITE).background(BColor.BLUE)   //setting format
                .build();	
	}
	
	public static void print(String message) {
		cp.debugPrintln(message);
		cp.debugPrintln(message, Attribute.BOLD, FColor.YELLOW, BColor.GREEN);
	}
	
}
