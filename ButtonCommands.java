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

class Reset extends ButtonCommands{

	Reset(Display d) {
		super(d);
		// TODO Auto-generated constructor stub
	}

	@Override
	void execute(int caseNum) {
		d.removeAll();
		d.init();
		
		
	}
	
}
