package chess.board;

import java.util.Scanner;

public class HumanInput {
	
public final static int startingCoordinate() {
	final int startingCoordinate;
	
	Scanner in=new Scanner(System.in);
	System.out.println("Pradine koordinate : ");
	startingCoordinate=in.nextInt();
	in.nextLine();
	return startingCoordinate;
}

public final static int destinationCoordinate() {
	final int destinationCoordinate;
	
	Scanner in=new Scanner(System.in);
	System.out.println("Galutine koordinate : ");
	destinationCoordinate=in.nextInt();
	in.nextLine();
	return destinationCoordinate;
}

}
