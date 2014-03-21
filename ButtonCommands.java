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


class VoltageOnOff extends ButtonCommands{

	VoltageOnOff(Display d) {
		super(d);
		// TODO Auto-generated constructor stub
	}
	
	void execute(int caseNum) {

		switch(caseNum%2){
		
		case 0:
			d.drawVoltage = true;
			d.voltageCalcing = true;
			d.voltageBarMax.setVisible(true);
			d.voltageBarMin.setVisible(true);
			break;
		case 1:
			d.drawVoltage = false;
			d.voltageCalcing = false;
			d.voltageBarMax.setVisible(false);
			d.voltageBarMin.setVisible(false);
			break;
		}
	}
	
}
