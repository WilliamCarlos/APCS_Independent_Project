public abstract class ButtonCommands{
	Display d;
	ButtonCommands(Display d){
		this.d = d;
	}
	
	abstract void execute(int caseNum);
}

class pauseBallMovement extends ButtonCommands{

	pauseBallMovement(Display d) {
		super(d);
		
	}

	@Override
	void execute(int caseNum) {

		switch(caseNum%2){
		
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
