package remoteController;

public interface RemoteListener {

	void turnLeft(RemoteEvent e);
	void turnRight(RemoteEvent e);
	void driveForward(RemoteEvent e);
	void driveReverse(RemoteEvent e);
}
