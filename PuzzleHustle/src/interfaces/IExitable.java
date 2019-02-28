package interfaces;

public interface IExitable {
	
	public void exitSubcribe(SubscribesToExitable subscriber);
	public void exitUnSubcribe(SubscribesToExitable subscriber);
	public void exit();

}
