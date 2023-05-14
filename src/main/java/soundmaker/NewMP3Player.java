package soundmaker;

import javazoom.jl.player.Player;

class PlayThread extends Thread {
	Player player = null;
	boolean isPause = false;

	public PlayThread(Player player) {
		this.player = player;
	}

	@Override
	public void run() {

		synchronized (player) {
			try {
				player.wait();
				player.play();
				player.notifyAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		while (true) {
			try {
				Thread.sleep(1000);
				if (isPause) {

					synchronized (player) {
						try {
							player.wait();
							player.notify();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
			} catch (InterruptedException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}

	}
}

class ControlThread extends Thread {
	Player player = null;
	boolean isPause;

	public ControlThread(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		for (int i = 0; i < 100000; i++) {
			try {
				if (i == 0) {
					synchronized (player) {
						try {
							player.notify();
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				}
				Thread.sleep(1000);

//				if (i == 6) {
//					synchronized (player) {
//						try {
//							player.wait();
//						} catch (Exception e) {
//							System.out.println(e);
//						}
//					}
//				}
				if (this.isPause == false) {
					player.notify();
				}

			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			System.out.println(player.getPosition());

		}
	}
}

public class NewMP3Player {
	private PlayThread playThread = null;
	private ControlThread controlThread = null;

	public void destroyThread() {
		this.playThread.stop();
		this.controlThread.stop();
	}

	public void controlPlayer(boolean isPause) {
		this.playThread.isPause = isPause;
		this.controlThread.isPause = isPause;
	}

	public NewMP3Player(Player player) {

		this.playThread = new PlayThread(player);
		this.controlThread = new ControlThread(player);

		playThread.start();
		controlThread.start();
	}

//	public static void main(String[] args) throws FileNotFoundException, JavaLayerException, InterruptedException {
//		String filename = System.getProperty("user.dir") + "/sound/1234.mp3";
//		BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filename));
//		Player player = new Player(buffer);
//
//		
//
//	}

}
