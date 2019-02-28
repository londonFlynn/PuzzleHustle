package interfaces;

public interface NewPuzzlePublisher {
	
	public void suscribe(NewPuzzleSubscriber subscriber);
	public void unSuscribe(NewPuzzleSubscriber subscriber);
	public void newPuzzle();

}
