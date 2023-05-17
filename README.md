# exp04

2023春信息系统开发(java)中期作业--音乐播放器客户端

## 软件功能说明

1. 该软件为一个简单的音乐播放器,主要功能如下
   - 获取远程服务器的共享歌单并渲染
   - 获取歌单中的音乐列表
   - 下载远程歌单中的音乐
   - 新建本地歌单并存储在本地服务器
   - 向本地服务器增添歌曲
   - 删除本地歌单
   - 获取本地歌单列表
   - 获取本地歌单中的音乐列表
   - 播放远程歌曲或者已经下载的远程歌曲或本地歌曲
   - 暂停歌曲和继续播放
   - 切换歌曲/切换不同歌单的歌曲
2. 软件使用注意事项
   - 因为本地数据库的创建使用了workbench可视化手动创建,所以用户想要使用本地歌单功能需要先创建数据库
   - 新建本地歌单时的封面路径需要手动录入
   - 向本地歌单中添加歌曲时尽量避免添加除了mp3以外的其他格式的文件,避免出现未知错误
   - 共享歌单中的封面和歌曲资源的下载路径为当前工作目录下的covers和songs ,如果因为文件夹不存在而产生了错误,请手动新建这两个文件夹
   - 因为能力和时间原因,仍有部分计划实现的功能没有实现,比如进度条和调节音量等基本的音乐属性的控制

## 流程框图

![流程框图](./assets/流程框图.png)

## 软件模块设计与实现

#### MP3Player.java 播放类

1. 主要用到的技术

   - **Synchronized**锁实现播放暂停
   - 实现了runnable接口以维护player对象

2. 类设计

   - 方法包括play ()播放和 暂停pause() 
   - 类为palyerRunnable 
   - 主要属性有: playlist 当前播放列表,存有音乐的地址(远程或本地都有)
   - curIndex 标记当前播放的歌曲的索引
   - isRemote 标记是否为远程播放
   - isPaused 标记当前状态是否暂停
   - lock 锁对象

3. 关键代码

   ```Java
   public void play() {
   		synchronized (lock) {
   			if (playerThread == null || !playerThread.isAlive()) {
   				playerThread = new Thread(new PlayerRunnable()); // 开一个子线程给playerThread维护
   				playerThread.start();
   			} else {
   				lock.notifyAll();
   			}
   			isPaused = false; // 更新 isPaused 状态
   		}
   	}
   
   	public void pause() {
   		synchronized (lock) {
   			isPaused = true; // 更新 isPaused 状态
   		}
   	}
   
   	private class PlayerRunnable implements Runnable {
   
   		@Override
   		public void run() {
   			try {
   				BufferedInputStream buffer;
   				// 判断是否为远程在线播放
   				if (isRemote) {
   					URL mp3url = new URL(filePath);
   					buffer = new BufferedInputStream(mp3url.openStream());
   				} else {
   					buffer = new BufferedInputStream(new FileInputStream(filePath));
   				}
   
   				player = new Player(buffer);
   
   				// 线程不中断就不会跳出循环
   				while (!Thread.currentThread().isInterrupted()) {
   					synchronized (lock) {
   						while (isPaused) {
   							lock.wait();
   						}
   					}
   					if (player.play(1) == false) {
   						break;
   					}
   				}
   
   			} catch (IOException | JavaLayerException | InterruptedException e) {
   				e.printStackTrace();
   			}
   		}
   	}
   ```

   

#### MusicDao.java和SheetDao.java 操作本地数据库的两个dao包

1. 包含的技术

   - 基本sql语句在java中的使用以及传值等

2. 类结构

   - insert(): 通过insert语句向歌单表或者歌曲表插入数据
   - delete(): 通过delete语句id删除歌单表中对应的歌单和歌曲表中的对应的歌曲
   - update(): 通过update语句更新指定歌单信息和歌曲信息
   - findAll(): 通过select语句 查询表中所有数据
   - findById(): 通过select语句查询指定id的歌单或歌曲
   - findBySheetId(): 通过select语句查询指定歌单id的所有歌曲
   - findByMd5(): 通过select语句查询指定md5的歌曲

3. 关键代码   

   因为findByMd5是我根据需求新添加的函数,所以就以此为例介绍一下相关方法的用法:    

   ```Java
   /**
   	 * @brief 歌曲md5查询歌曲
   	 * @param md5 欲查询的歌曲的md5
   	 * @return music 对象
   	 */
   	public Music findByMd5(String md5) {
   		Connection conn = null; // conn 对象 用于连接数据库
   		PreparedStatement ps = null; // 预编译的SQL语句 执行参数化的更新和查询操作
   		ResultSet rs = null; // 结果集
   		Music music = null; // 结果对象
   		String sql = "SELECT ID, NAME, sheet_id, MD5, file_path FROM music WHERE MD5=?";
   		try {
   			conn = SqliteUtil.getConnection();
   			ps = conn.prepareStatement(sql);
   			ps.setString(1, md5); // 将md5插入到第一个占位符 ?
   			rs = ps.executeQuery(); // 执行sql语句
   			if (rs.next()) { // 若结果集不为空将结果依次赋值给music对象
   				music = new Music();
   				music.setId(rs.getInt(1));
   				music.setName(rs.getString(2));
   				music.setSheetId(rs.getInt(3));
   				music.setMd5(rs.getString(4));
   				music.setFilePath(rs.getString(5));
   			}
   		} catch (SQLException e) {
   			e.printStackTrace();
   		} finally {
   			SqliteUtil.close(null, ps, conn);
   		}
   		return music;
   	}
   ```

   

#### SqliteUtil工具类

1. 技术简介

   - 数据库驱动的安装与导入
   - 链接数据库的相关方法
   - 创建表和查询表

2. 类结构

   - getConnection(): 链接目标数据库并返回一个connection对象
   - close(): 关闭数据库

3. 关键代码

   ```java
   	static {
   		try {
   			Class.forName("com.mysql.cj.jdbc.Driver"); // 导入驱动
   		} catch (ClassNotFoundException e) {
   			e.printStackTrace();
   		}
   	}
   /**
   	 * 获取数据库连接
   	 * 
   	 * @return
   	 */
   	public static Connection getConnection() {
   		Connection conn = null;
   		try {
   			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/music-player?user=root&password=admin123"); // 链接本地数据库中的 music-player库
   			System.out.println("Database Opened.");
   		} catch (SQLException e) {
   			e.printStackTrace();
   			System.out.println("Database Connection failed.");
   		}
   
   		return conn;
   	}
   
   	/**
   	 * 关闭数据库连接
   	 */
   	public static void close(ResultSet rs, Statement stmt, Connection conn) {
   		try {
   			if (rs != null)
   				rs.close();
   			if (stmt != null)
   				stmt.close();
   			if (conn != null)
   				conn.close();
   		} catch (SQLException e) {
   			e.printStackTrace();
   		}
   		System.out.println("Database Closed.");
   	}
   ```

   

#### MusicSheetTaker获取共享歌单类

1. 技术简介

   - httpclient包
   - hashMap
   - 基本的流的读入

2. 类结构

   - queryMusicSheets() 请求目标地址的歌单返回 并封装成需要的结构渲染
   - convertStreamToString(): 将inputStream转换成String返回

3. 关键代码

   ```Java
   /**
   	 * 查询所有音乐单
   	 * 
   	 * @param url api地址
   	 * @param port 端口号
   	 * @return List<MusicSheet> 类型为 MusicSheet的list
   	 * @throws HttpException
   	 * @throws IOException
   	 */
   	public static List<MusicSheet> queryMusicSheets(String url, int port) throws HttpException, IOException {
   
   		HttpClient client = new HttpClient();
   		client.getHostConfiguration().setHost(url, port);
   		GetMethod method = new GetMethod(url);
   		client.executeMethod(method);
   
   		// 打印服务器返回的状态
   		System.out.println(method.getStatusLine());
   
   		// 打印返回的信息
   		InputStream bodystreams = method.getResponseBodyAsStream();
   		JSONObject jsonBody = new JSONObject(convertStreamToString(bodystreams));
   		JSONArray jsonMusicSheetList = (JSONArray) jsonBody.get("musicSheetList");
   
   		JSONObject jms = null;
   		List<MusicSheet> mss = new ArrayList<MusicSheet>();
   		MusicSheet ms = null;
   		Map<String, String> mum = null;
   
           // 循环得到的musicSheetList 依次add到mss数组中
   		for (Object musicSheetObj : jsonMusicSheetList) {
   			jms = new JSONObject(musicSheetObj.toString());
   			ms = new MusicSheet();
   			ms.setUuid((String) jms.get("uuid"));
   			ms.setName((String) jms.get("name"));
   			ms.setCreator((String) jms.get("creator"));
   			ms.setCreatorId((String) jms.get("creatorId"));
   			ms.setDateCreated((String) jms.get("dateCreated"));
   			ms.setPicture((String) jms.get("picture"));
   			// 做类型转化
   			JSONObject mumObj = (JSONObject) jms.get("musicItems");
   			Iterator<String> keys = mumObj.keys();
   			mum = new HashMap<String, String>();
   			// 设置key为md5 value为文件名的映射
   			while (keys.hasNext()) {
   				String key = keys.next();
   				mum.put(key, mumObj.getString(key));
   			}
   			ms.setMusicItems(mum);
   			mss.add(ms);
   		}
   
   		// 释放连接
   		method.releaseConnection();
   		return mss;
   	}
   ```



#### FileDownloader文件下载类(包括歌单封面和歌曲的下载)

1. 技术简介

   - httpclient包
   - 数据流的处理

2. 类结构

   - downloadMusicFile(): 下载音乐文件
   - downloadMusicSheetPicture(): 下载歌单封面

3. 关键代码

   ```java
   public static void downloadMusicFile(String url, String fileMd5, String targetPath, String fileName) {
   		HttpClient client = new HttpClient();
   		GetMethod get = null;
   		FileOutputStream output = null;
   		String filename = null;
   		try {
   			get = new GetMethod(url + "?md5=" + fileMd5); // 拼接 目标 api地址
   			int i = client.executeMethod(get);
   
   			if (SUCCESS == i) {
   				if (fileName == null) {
                       // 通过响应头解析文件名
   					filename = java.net.URLDecoder
   							.decode(get.getResponseHeader("Content-Disposition").getValue().substring(21), "UTF-8");
   					System.out.println("[The file name getting from HTTP HEADER] " + filename);
   				} else {
   					filename = fileName;
   				}
   
   				File storeFile = new File(targetPath + "/" + filename);
   				output = new FileOutputStream(storeFile);
   				output.write(get.getResponseBody()); // 将文件流流写入目标地址
   			} else {
   				System.out.println("DownLoad file failed with error code: " + i);
   			}
   		} catch (Exception e) {
   			e.printStackTrace();
   		} finally {
   			try {
   				if (output != null) {
   					output.close();
   				}
   			} catch (IOException e) {
   				e.printStackTrace();
   			}
   
   			get.releaseConnection();
   			client.getHttpConnectionManager().closeIdleConnections(0);
   		}
   	}
   
   	public static void downloadMusicSheetPicture(String url, String musicSheetUuid, String targetPath) {
   		HttpClient client = new HttpClient();
   		GetMethod get = null;
   		FileOutputStream output = null;
   		String filename = null;
   
   		try {
   			get = new GetMethod(url + "?uuid=" + musicSheetUuid); // 根据uuid获取歌单封面
   			int i = client.executeMethod(get);
   
   			if (SUCCESS == i) {
                   // 通过响应头解析文件名字
   				filename = java.net.URLDecoder
   						.decode(get.getResponseHeader("Content-Disposition").getValue().substring(21), "UTF-8");
   				System.out.println("[The file name getting from HTTP HEADER] " + filename);
   
   				File storeFile = new File(targetPath + "/" + filename);
   				output = new FileOutputStream(storeFile);
   				output.write(get.getResponseBody()); // 写入目标地址
   			} else {
   				System.out.println("DownLoad file failed with error code: " + i);
   			}
   		} catch (Exception e) {
   			e.printStackTrace();
   		} finally {
   			try {
   				if (output != null) {
   					output.close();
   				}
   			} catch (IOException e) {
   				e.printStackTrace();
   			}
   
   			get.releaseConnection();
   			client.getHttpConnectionManager().closeIdleConnections(0);
   		}
   	}
   ```

   

#### 封装自定义table

1. 重写类

   - 重新封装了JTable防止双击可以编辑

2. 代码

   ```java
   public class MyTable extends JTable {
   
   	private static final long serialVersionUID = 1L;
   
   	// 重写JTable类的构造方法
   	public MyTable(DefaultTableModel tableModel) {// Vector rowData, Vector columnNames
   		super(tableModel); // 调用父类的构造方法
   	}
   
   	public MyTable(final Object[][] rowData, final Object[] columnNames) {
   		super(rowData, columnNames);
   	}
   
   	// 重写JTable类的getTableHeader()方法
   	public JTableHeader getTableHeader() { // 定义表格头
   		JTableHeader tableHeader = super.getTableHeader(); // 获得表格头对象
   		tableHeader.setReorderingAllowed(false); // 设置表格列不可重排
   		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer(); // 获得表格头的单元格对象
   		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 设置列名居中显示
   		return tableHeader;
   	}
   
   	// 重写JTable类的getDefaultRenderer(Class<?> columnClass)方法
   	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) { // 定义单元格
   		DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super.getDefaultRenderer(columnClass); // 获得表格的单元格对象
   		cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER); // 设置单元格内容居中显示
   		return cr;
   	}
   
   	// 重写JTable类的isCellEditable(int row, int column)方法
   	public boolean isCellEditable(int row, int column) { // 表格不可编辑
   		return false;
   	}
   }
   ```



#### 添加歌单的gui模态框

1. 技术简介

   - 主要使用了swing的jdialog以及基本的jbutton jlabel 和 jtextfiled 表单
   - SimpleDateFormat函数用于时间格式化  

2. 类结构

   - 主类继承了 JDialog
   - 属性有歌曲名的表单域和歌单封面的路径
   - 主要在init函数里写了dialog的基本布局
   - 在actionPerformed中写了歌单的创建
   - 创建时间是自己封装了一个getNowDate的函数 

3. 关键代码

   ```java
   public void actionPerformed(ActionEvent e) {
   		String name = String.valueOf(nameField.getText()); // 获取歌单名的表单域中的内容
   		String pic_path = String.valueOf(picField.getText()); // 获取歌单封面的路径
   		String creator = "Raining"; // 定义 creator 为 Raining (因为跟我名字谐音哈哈哈)
   		String dateCreated = getNowDate(); // 获取当前时间 格式为 yyyy-MM-dd
   		newSheet = new Sheet(); // 创建sheet对象以插入数据库
   		newSheet.setName(name);
   		newSheet.setCreator(creator);
   		newSheet.setDateCreated(dateCreated);
   		newSheet.setPicPath(pic_path);
   		SheetDao sheetDao = new SheetDao();
   		if (name.length() != 0 && pic_path.length() != 0) {
   			sheetDao.insert(newSheet); // 调用sheetDao包中的insert函数
   			app.refreshLocalSheet(); // 调用主gui组件中封装的刷新视图的函数
   		}
   		this.setVisible(false); // 关闭当前dialog
   
   	}
   ```

   

#### **不同gui组件之间的通信问题**

> gui和一些ajax请求的类都是使用老师封装好的类,写的最多的还是数据的获取以及渲染到gui中和一些事件的绑定

- 我将app 也就是MusicPlayerGUI(以下都称作app)作为通信的媒介,以保证其中的子组件可以调用app中的某些变量或方法以达到通信的目的

- 其中app中方便通信的函数

  - refreshDisplaySheet() 刷新歌单展示区
  - refreshLocalSheet() 刷新本地歌单
  - playMusic(): 从头开始播放的函数
  - playNext(): 播放当前歌单的下一首
  - playPre(): 播放上一首

- 一些关键代码

  ```java
  // 播放音乐 (从头开始播放)
  	public void playMusic() {
  		isRemote = false;
  		musicPlayer.setTableSelectedRow(curPlayIndex);
  		Object[] keys = this.curPlaySheet.getMusicItems().keySet().toArray(); // 获取所有的 md5
          // 判断是否为本地
  		if (this.isLocal) {
  			String[] playlist = new String[keys.length];
  			MusicDao mDao = new MusicDao();
  			for (int i = 0; i < keys.length; ++i) {
  				String md5 = keys[i].toString();
  				Music m = mDao.findByMd5(md5); // 根据md5找到数据库中的歌曲信息
  				String path = m.getFilePath(); // 将歌曲的地址存入playlist 
  				playlist[i] = path;
  			}
  			if (mp3Player != null) // 如果当前正在播放 暂停
  				mp3Player.pause();
              // 新建一个mp3player对象 将地址列表和curIndex传入 并将 this传入以使得其可以调用当前组件的函数
  			mp3Player = new MP3Player(playlist, curPlayIndex, this);
  			musicPlayer.setPauseText(); // 设置播放按钮为 "暂停"
  			mp3Player.play(); // 开始播放
  
              // 如果是远程歌曲
  		} else {
              // 先获取当前歌曲的具体路径看看是否已经下载
  			String curMD5 = keys[this.curPlayIndex].toString(); 
  			String basicPath = System.getProperty("user.dir") + "\\songs\\";
  			String filepath = basicPath + this.curPlaySheet.getMusicItems().get(curMD5);
  			File file = new File(filepath);
  			// 如果已经下载 播放本地 操作与本地歌单类似
  			if (file.exists()) {
  				String[] playlist = new String[keys.length];
  				for (int i = 0; i < keys.length; ++i) {
  					String md5 = keys[i].toString();
  					String path = basicPath + this.curPlaySheet.getMusicItems().get(md5);
  					playlist[i] = path;
  				}
  				if (mp3Player != null)
  					mp3Player.pause();
  				mp3Player = new MP3Player(playlist, curPlayIndex, this);
  
  				musicPlayer.setPauseText();
  				mp3Player.play();
  			} else {
  				// 如果没有下载 播放 线上
                  // 更新 isRemote 标记
                  // 也与上述本地类似 除了basicPath
  				isRemote = true;
  				String[] playlist = new String[keys.length];
  				String remoteBasicPath = "http://119.167.221.16:38080/music.server/music?md5=";
  				for (int i = 0; i < keys.length; ++i) {
  					String md5 = keys[i].toString();
  					String path = remoteBasicPath + md5;
  					playlist[i] = path;
  				}
  				if (mp3Player != null)
  					mp3Player.pause();
  				mp3Player = new MP3Player(playlist, curPlayIndex, this);
  
  				musicPlayer.setPauseText();
  				mp3Player.play();
  			}
  		}
  	}
  ```



#### 为本地歌单添加歌曲

- 主要使用了JFileChooser对象用于选择音乐文件

- 代码

  ```java
  // 绑定添加歌曲的点击事件
  addMusic.addActionListener(new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				JFileChooser fileChooser = new JFileChooser();
  				int res = fileChooser.showOpenDialog(app); // 传入app以在app下显示dialog
  				if (res == JFileChooser.APPROVE_OPTION) {
  					File selectedFile = fileChooser.getSelectedFile();
  					String filePath = selectedFile.getAbsolutePath(); // 获取文件地址
  					int sheetId = app.getLocalSheet().curSheet.getId();
  					String name = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.indexOf('.')); // 通过'\' 和 '.' 分理出文件名
  
  					FileInputStream fis;
  					String Md5 = null;
  					try {
  						fis = new FileInputStream(filePath);
  						Md5 = DigestUtils.md5Hex(IOUtils.toByteArray(fis)); // 生成歌曲md5
  
  					} catch (IOException e1) {
  						e1.printStackTrace();
  					}
  					Music music = new Music();
  					music.setName(name);
  					music.setSheetId(sheetId);
  					music.setFilePath(filePath);
  
  					music.setMd5(Md5);
  					mDao.insert(music); // 向数据库插入新加入的歌曲
  					Map<String, String> mum = musicSheet.getMusicItems(); // 获取当前歌单的map 将新添加的加入
  					mum.put(Md5, name);
  					musicSheet.setMusicItems(mum);
  					app.refreshDisplaySheet(musicSheet); // 刷新当前组件
  
  				}
  			}
  		});
  ```

  

## 软件运行演示

1. 播放方面: 主要演示了播放本地和在线播放 下载歌曲 以及暂停继续控制下一首上一首的切换
2. 添加歌单
3. 为歌单添加歌曲 以及播放
4. 上述操作都进行了自动刷新的操作

<video src="./assets/演示.webm"></video>

>  如果video不渲染,请访问[演示视频](./assets/演示.webm)



## 项目目录结构

```
├─pom.xml 
├─src
|  ├─main
|  |  ├─java
|  |  |  ├─utils
|  |  |  |   ├─MP3Util.java
|  |  |  |   └SqliteUtil.java					
|  |  |  ├─system
|  |  |  |   └Constant.java
|  |  |  ├─soundmaker
|  |  |  |     ├─DrawPanel.java
|  |  |  |     ├─MP3Player.java							// 音乐播放的核心类
|  |  |  |     ├─NewMP3Player.java
|  |  |  |     ├─PlayerThread.java
|  |  |  |     ├─WaveFileDrawDemo.java
|  |  |  |     ├─WaveFileReader.java
|  |  |  |     └WavePlayer.java
|  |  |  ├─Raining
|  |  |  |    ├─ouc_music_player
|  |  |  |    |        └App.java
|  |  |  ├─player
|  |  |  |   ├─Player.java
|  |  |  |   ├─TestPlayer.java
|  |  |  |   └XMusic.java
|  |  |  ├─musicclient
|  |  |  |      └MusicOperationClient.java
|  |  |  ├─model
|  |  |  |   ├─Music.java
|  |  |  |   └MusicSheet.java
|  |  |  ├─images
|  |  |  |   └music.png
|  |  |  ├─httpclient
|  |  |  |     ├─FileDownloader.java
|  |  |  |     ├─HttpPostWithJSON.java
|  |  |  |     ├─MusicSheetAndFilesUploader.java
|  |  |  |     ├─MusicSheetTaker.java
|  |  |  |     └MusicSheetUploader.java
|  |  |  ├─gui
|  |  |  |  ├─AddSheetGui.java							// 添加本地歌单的模态框gui
|  |  |  |  ├─LocalMusicSheetBlock.java
|  |  |  |  ├─MusicPlayerBlock.java
|  |  |  |  ├─MusicPlayerGUI.java
|  |  |  |  ├─MusicSheetDisplayBlock.java
|  |  |  |  ├─MusicSheetManagementBlock.java
|  |  |  |  ├─MyTable.java								// 二次封装 默认禁止双击编辑
|  |  |  |  ├─NewMusicSheetDisplayBlock.java
|  |  |  |  └SharedMusicSheetBlock.java
|  |  |  ├─db_model
|  |  |  |    ├─Music.java
|  |  |  |    └Sheet.java
|  |  |  ├─dao
|  |  |  |  ├─BaseDao.java
|  |  |  |  ├─MusicDao.java
|  |  |  |  └SheetDao.java
├─songs													// 保存从服务器下载下来的歌曲
├─covers												// 保存下载下来的封面
├─assets
|   ├─1-首页.png
|   ├─2-创建本地歌单.png
|   ├─3-歌单添加成功.png
|   ├─4-向歌单中添加歌曲.png
|   ├─5-添加歌曲成功.png
|   └6-播放.png

```

> 其中songs  和 covers 为远程歌曲的歌曲下载路径和封面下载路径






## 开发期间遇到的问题以及如何解决的

1. 各个gui组件之间的通讯问题:
	基本都用根组件 MusicPlayerGUI做了父子通讯
2. 本地数据库不能创建的问题
	因为我用的是mysql,所以有部分语法不匹配,干脆直接使用了workbench可视化手动创建了数据库和表
3. 播放暂停功能:
	卡了很久,最后还是写的多线程处理的播放暂停,期间也有一些不太合理的地方,都做了修改

### 软件运行截图

主页

![1-首页](./assets/1-首页.png)

添加本地歌单

![2-创建本地歌单](./assets/2-创建本地歌单.png)

![3-歌单添加成功](./assets/3-歌单添加成功.png)

为本地歌单添加歌曲

![4-向歌单中添加歌曲](./assets/4-向歌单中添加歌曲.png)

![5-添加歌曲成功](./assets/5-添加歌曲成功.png)

播放

![6-播放](./assets/6-播放.png)
