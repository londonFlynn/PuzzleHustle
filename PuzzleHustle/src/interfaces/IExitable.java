package interfaces;

public interface IExitable {
	
	public void suscribe(SubscribesToExitable subscriber);
	public void unSuscribe(SubscribesToExitable subscriber);
	public void exit();

}
