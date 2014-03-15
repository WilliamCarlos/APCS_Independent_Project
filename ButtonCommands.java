public abstract class ButtonCommands extends Main{

	Display d = getDisplay(); //This takes the display from Main class. AKA the same display thats running.
	abstract void execute(int caseNum);
}

class pauseBallMovement extends ButtonCommands{

	@Override
	void execute(int caseNum) {

		switch(caseNum){
		
		case 0:
			d.ballsMoving = true;
			d.voltageCalcing = true;
			break;
		case 1:
			d.ballsMoving = false;
			d.voltageCalcing = false;
			break;
		}
	}
	
}