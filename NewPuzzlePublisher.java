package interfaces;

public interface NewPuzzlePublisher {
	
	public void subcribe(NewPuzzleSubscriber subscriber);
	public void unSubcribe(NewPuzzleSubscriber subscriber);
	public void newPuzzle();

}
