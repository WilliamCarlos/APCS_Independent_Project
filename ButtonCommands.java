public abstract class ButtonCommands{
	initialDisplay d;
	ButtonCommands(initialDisplay d){
		this.d = d;
	}
	
	abstract void execute(int caseNum);
}

class pauseBallMovement extends ButtonCommands{

	

	pauseBallMovement(initialDisplay d) {
		super(d);
		// TODO Auto-generated constructor stub
	}

	@Override
	void execute(int caseNum) {

		switch(caseNum%2){
		
		case 0:
			d.ballsMoving = true;
			
			break;
		case 1:
			d.ballsMoving = false;
			
			break;
		}
	}
	
}

class Reset extends ButtonCommands{

	

	Reset(initialDisplay d) {
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

	
	VoltageOnOff(initialDisplay d) {
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

class toogleElasticWalls extends ButtonCommands{


	toogleElasticWalls(initialDisplay d) {
		super(d);
		// TODO Auto-generated constructor stub
	}

	@Override
	void execute(int caseNum) {
		switch(caseNum%2){
		case 0:
			d.elasticWalls = false;
			break;
		case 1:
			d.elasticWalls = true;
			break;
		}
		
	}
	
}
