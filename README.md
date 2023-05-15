# exp04

音乐播放器客户端

## 主要功能实现

1. 获取共享歌单列表以及歌单相关信息
2. 下载歌曲和封面
3. 添加本地歌单和删除本地歌单
4. 实现了播放暂停等相关功能

## 目录结构

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

### 一些运行截图

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
