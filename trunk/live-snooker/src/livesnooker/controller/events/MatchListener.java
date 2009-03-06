package livesnooker.controller.events;

public interface MatchListener {
	void matchStarted(FrameEvent e);
	void matchEnded(FrameEvent e);
}
