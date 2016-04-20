package orientsna.engine.algorithms;

public interface Algorithm {
	public abstract String getName();

	public abstract <T> T getResult();
}
