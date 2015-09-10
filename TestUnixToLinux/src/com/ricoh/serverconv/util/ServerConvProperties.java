package com.ricoh.serverconv.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ServerConvProperties {
    static ResourceBundle bundle = null;
	//DirectoryGetterImpl
	public static final String HOME = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'find /export/home/{} -type d -prune' | xargs -I{} sh -c 'ls -ld {} 2> /dev/null'";
	public static final String COMMAND = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'find /export/home/{}/* -type d -prune' | xargs -I{} sh -c 'ls -ld {} 2> /dev/null'";
	public static final String SSHDIR = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'find /export/home/{}/.ssh -type d -prune' | xargs -I{} sh -c 'ls -ld {} 2> /dev/null'";
	public static final String AUTHFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'find /export/home/{}/.ssh/authorized_keys' | xargs -I{} sh -c 'ls -ld {} 2> /dev/null'";
	public static final String CPPROFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'ls -ltd /export/home/{} 2> /dev/null' | cut -d' ' -f10 | xargs -I{} sh -c 'cp -f /home/vagrant/.bash* {}'";
	public static final String CHOWNPROFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'ls -ltd /export/home/{} 2> /dev/null' | cut -d' ' -f3,4,10 | sed s/' '/':'/ | xargs -I{} sh -c 'chown {}/.bash*'";
	public static final String CSHOWN = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'ls -ltd /export/home/{} 2> /dev/null' | cut -d' ' -f3,4,10 | sed s/' '/':'/ | xargs -I{} sh -c 'chown {}/.csh* 2> /dev/null'";
	//EnvPrePareImpl
	public static final String EPEL = "rpm -ivh http://ftp.riken.jp/Linux/fedora/epel/6/x86_64/epel-release-6-8.noarch.rpm";
	public static final String HOME_CHANGE_DIR =  "useradd -D -b /export/home";
	public static final String CHAR_CHANGE =  "sed -i -e 's/en_US.UTF-8/ja_JP.eucJP/g' /etc/sysconfig/i18n";
	public static final String YUM_INSTALL = "yum -y install @@@";
	public static final String SERVICE_ON = "chkconfig @@@ on";
	//NetWorkGetterImpl
	public static final String SSH_DIR = "/etc/ssh/";
	public static final String CF_COMMAND = "cat " + SSH_DIR + "sshd_config";
	//SambaGetterImpl
	public static final String SAMBA_DIR = "/etc/sfw/";
	public static final String SAMBACOMMAND = "cat " + SAMBA_DIR + "smb.conf";
	//SendMailGetterImpl
	public static final String SENDMAIL_DIR = "/etc/mail/";
	public static final String MC_COMMAND = "cat " + SENDMAIL_DIR + "sendmail.mc";
	public static final String AC_COMMAND = "cat " + SENDMAIL_DIR + "access";
	public static final String LC_COMMAND = "cat " + SENDMAIL_DIR + "local-host-names";
	public static final String ALIAS_COMMAND = "cat " + SENDMAIL_DIR + "aliases";
	public static final String MAILER_COMMAND = "cat " + SENDMAIL_DIR + "mailertable";
	public static final String VIRTUSE_COMMAND = "cat " + SENDMAIL_DIR + "virtusertable";
	//SystemInfoGetterImpl
	public static final String ENV = "env";
	public static final String HOSTS = "cat /etc/hosts";
	public static final String HOSTS_ALLOW = "cat /etc/hosts.allow";
	public static final String RESOLV_CONF = "cat /etc/resolv.conf";
	public static final String NSSW_CONF = "cat /etc/nsswitch.conf";
	public static final String SYSLOG = "cat /etc/syslog.conf";
	public static final String NTP_CONF = "cat /etc/inet/ntp.conf";
	//UserGetterImpl
	public static final  String PA = "/etc/passwd";
	public static final  String GR = "/etc/group";
	public static final  String SH = "/etc/shadow";
	public static final  String AU = ".ssh/authorized_keys";
	public static final  String PASSWD = "cat " + PA;
	public static final  String GROUP = "cat " + GR;
	public static final  String SHADOW = "cat " + SH;
	public static final  String CRON = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'echo :::{};crontab -l {} 2> /dev/null'";
	public static final  String SSH = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'echo :::{};cat /export/home/{}/" + AU + " 2> /dev/null'";
	public static final  String PROFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'echo :::{};cat /export/home/{}/.profile 2> /dev/null'";
	public static final  String CSHPROFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'echo :::{};cat /export/home/{}/.cshrc 2> /dev/null'";
	public static final  String BSHPROFILE = "cat /etc/passwd | cut -d':' -f1 | xargs -I{} sh -c 'echo :::{};cat /export/home/{}/.bashrc 2> /dev/null'";
	//OracleGetterImpl
	public static final  String TNSNAMES_DIR = "/export/home/oracle/app/oracle/product/9.2.0/network/admin/";
	public static final  String TNSNAMES = "tnsnames.ora";
	public static final  String ORACLEZIP = "linux.x64_11gR2_client.zip";
	public static final  String INSTALLRSP = "client_install.rsp";
	public static final  String INSTALLCMD = "sudo -u oracle /export/home/oracle/client/runInstaller -silent -responseFile /export/home/oracle/client_install.rsp  &> /dev/null";
    
    
	public static final String[] servers = {"news", "centos65"};
//	public static final String[] servers = {"mars", "venus","news", "vagrant", "centos65"};
	
	public static  final Map<String, String> converMap =new HashMap<String, String>(){{ 
//		put("mars","vagrant");
		put("news","centos65");
	}};
	
	public static final List<String> moduleList =new ArrayList<String>(){{ 
		add("system");
		add("user");
		add("dir");
		add("sendmail");
		add("samba");
		add("network");
		add("httpd");
		add("oracle");
	}};
	
	public static final List<String[]> convSenarioList =new ArrayList<String[]>(){{ 
		add(new String[] {"system","hosts"});
		add(new String[] {"user","group"});
		add(new String[] {"user","passwd"});
		add(new String[] {"user","shadow"});
		add(new String[] {"dir","home"});
		add(new String[] {"dir","dir"});
		add(new String[] {"dir","ssh_dir"});
		add(new String[] {"dir","auth"});
		add(new String[] {"user","auth"});
//		add(new String[] {"user","cron"});
		add(new String[] {"user","bash"});
		add(new String[] {"user","csh"});
		add(new String[] {"dir","csh"});
////		add(new String[] {"httpd",null});
		add(new String[] {"sendmail",null});
		add(new String[] {"samba",null});
		add(new String[] {"network",null});
		add(new String[] {"oracle",null});
		}};
		
    public static String getProperties(String key) {
		return bundle.getString(key);
	}

	public static String setProperties(String prop_name) {
	      try {
	          bundle = ResourceBundle.getBundle(prop_name);
	      } catch (MissingResourceException e) {
	          e.printStackTrace();
	          return null;
	      }
		
		return null;
	}
	
	public synchronized static int calcPermission(String permit) {
		int permitvalue = 0;
		String own = permit.trim().substring(1, 4);
		String group = permit.trim().substring(4,7);
		String etc = permit.trim().substring(7,10);
		
		if (own.contains("r")) {
			permitvalue = 400;
		}
		if (own.contains("w")) {
			permitvalue = permitvalue + 200;
		}
		if (own.contains("x")) {
			permitvalue = permitvalue + 100;
		}
		if (group.contains("r")) {
			permitvalue = permitvalue + 40;
		}
		if (group.contains("w")) {
			permitvalue = permitvalue + 20;
		}
		if (group.contains("x")) {
			permitvalue = permitvalue + 10;
		}
		if (etc.contains("r")) {
			permitvalue = permitvalue + 4;
		}
		if (etc.contains("w")) {
			permitvalue = permitvalue + 2;
		}
		if (etc.contains("x")) {
			permitvalue = permitvalue + 1;
		}
		
		return permitvalue;
	}
	

	public static final Map<String, String> SshdMap =new HashMap<String, String>(){{ 
		put("Protocol","サーバ側でサポートするSSH のバージョン");
		put("Port","SSH が接続を受けるポート番号");
		put("ListenAddress","SSH が接続を受け付けるローカルアドレス");
		put("AllowTcpForwarding","TCP 転送の許可。（default：yes）");
		put("GatewayPorts","ポート中継の許可。（default：no）");
		put("X11Forwarding","X11転送の許可。（default：yes）");
		put("X11DisplayOffset","X11が転送するディスプレイ番号。（default：10）");
		put("X11UseLocalhost","X11 をlocalhost のみ許可。（default：yes）");
		put("PrintMotd","ログイン時に/etc/motd ファイルの内容を表示。（default：yes）");
		put("KeepAlive","キープアライブメッセージを送信する。（default：yes）");
		put("SyslogFacility","sshd が出力するログメッセージの分類コード。（default：AUTHPRIV）");
		put("LogLevel","sshd が出力するログメッセージの冗長性レベル。");
		put("HostKey","SSH で使われる秘密鍵が格納されるファイル");
		put("Ciphers","");
		put("MACS","");
		put("ServerKeyBits","バージョン１で使われる鍵のビット数。（default：768bit）");
		put("KeyRegenerationInterval","バージョン１で鍵を再生成する間隔。（default：3600秒）");
		put("StrictModes","ログイン前にユーザのディレクトリやファイルのパーミッションのチェック。（default：yes）");
		put("LoginGraceTime","ログインの際の猶予時間。（default：120秒）");
		put("MaxAuthTries","１接続あたりの最大認証回数。（default：6）");
		put("MaxAuthTriesLog","");
		put("PermitEmptyPasswords","空のパスワードを許可。（default：no）");
		put("PasswordAuthentication","パスワード認証を許可。（default：yes）");
		put("PAMAuthenticationViaKBDInt","パスワード認証を許可。（default：yes）");
		put("PermitRootLogin","root のログイン制限。（default：no）");
		put("Subsystem","サブシステムを設定");
		put("AllowUsers","記述されたユーザのみ許可。それ以外は拒否");
		put("IgnoreRhosts","~/.rhosts 及び~/.shosts を使用。（default：yes）");
		put("RhostsAuthentication","rhost認証を許可（default：yes）");
		put("RhostsRSAAuthentication","RSA 認証が成功した時、rhosts を使った認証を許可。（default：no）");
		put("RSAAuthentication","RSA 認証の許可。（default：yes）");
	}};
	public static  final Map<String, String> SmbMap =new HashMap<String, String>(){{ 
		put("workgroup","サーバのサービスするグループ名称です。");
		put("server string","ブラウズしたときに表示されるサーバの詳細説明");
		put("hosts allow","アクセスを許すホスト");
		put("load printers ","プリンタを使用可能");
		put("printing","プリンタを使用可能");
		put("printcap name","プリンタを使用可能");
		put("printer name","プリンタを使用可能");
		put("log file","ユーザやマシン毎にログを取りたい場合に利用");
		put("log lebel","ログのレベルを指定");
		put("max log size","ログのファイルの大きさの上限を指定");
		put("security user","セキュリティに関係");
		put("password level","パスワードトライ文字数");
		put("wins support ","WINS サーバをエミュレート");
		put("browseable ","ブラウズリスト中の有効な共有一覧に表示指定");
		put("preserve case ","大文字・小文字に関係する部分");
		put("short preserve case ","大文字・小文字に関係する部分");
		put("case sensitive","大文字・小文字に関係する部分");
		put("coding system","サーバ上で利用する漢字コード");
		put("client code page","日本語コードを用いる場合の指定。");
		put("wide links","ディレクトリの外部へのリンクの許可の指定。");
		put("hide dot files ","先頭がドットで始まるファイルを隠す場合の指定。");
		put("getwd cache ","キャッシュに関する指定。");
		put("comment","ブラウズした際の説明文");
		put("browseable","ユーザのホームディレクトリは一般には一覧不可にする。");
		put("writable ","書き込み許可。");
		put("create mode","ファイル作成時のモード。");
		put("dos charset","Samba が DOS クライアントと通信する際に用いられる文字コードセットを指定");
		put("unix charset","Samba が動作する UNIX マシンで使われている文字コードを指定");
		put("display charset","Samba がメッセージを標準出力や標準エラー出力に出力する際に 用いる文字コードセットを指定");
		put("passdb expand explicit","passdb の指定に % マクロが明示的に指定された際に、 それを展開するかどうかを制御");
		put("valid users","サービスにアクセスできるユーザーのリストを指定");
		put("veto files","一覧することもアクセスすることもできないファイルとディレクトリのリストを指定");
		put("path","サービスに接続したユーザーがアクセスするディレクトリを指定");
		put("force user","サービスに対する接続のすべてで、 デフォルトのユーザーとみなされる UNIX ユーザーの名前を指定");
		put("force group","サービスに接続するすべてのユーザーのデフォルトの所属グループと見なされる UNIX のグループ名を指定");
		put("read only"," yes の場合、 ユーザーは、サービスのディレクトリ内におけるファイルの作成も修正もできない");
		put("guest ok"," yes のサービスに付いては、 サービスへの接続の際にパスワードが要求されない");
		put("directory mask","パーミッションを示す8進数の数値であり、 UNIX 側のディレクトリ作成時に DOS の属性から UNIX のパーミッションを生成する際に使われる");
		put("hide special files","クライアントからソケット、デバイス、FIFOなどのファイルがディレクトリ一覧で表示されることを抑止");
		put("hide unreadable","読みとりができないファイルの存在をクライアントから隠蔽できる。 このパラメーターのデフォルトは off");
		put("hide unwriteable files","書き込みができないファイルの存在をクライアントから隠蔽できる。 このパラメーターのデフォルトは off ");
		put("map archive","DOSのアーカイブ属性を UNIXの所有者(owner)実行権ビットに割り当てるかどうかを決定する");
	}};
	
	public static  final Map<String, String> MailMcMap =new HashMap<String, String>(){{ 
		put("MASQUERADE_AS","メールヘッダー「From:」の送信者アドレスを設定値に書き換える");
		put("FEATURE_masquerade_entire_domain","SMTPで相手サーバーと通信する際のメールアドレスも全て書き換える");
		put("FEATURE_limited_masquerade","MASQUERADE_DOMAINに、記述されたドメインのみ MASQUERADE_AS の対象");
		put("FEATURE_allmasquerade","受信側（宛先）に対してもマスカレードさせる");
		put("FEATURE_use_cw_file","/etc/mail/local-host-namesを読むように指定");
		put("FEATURE_mailertable","/etc/mail/mailertableを読むように指定");
		put("FEATURE_access_db","設定値を読むように指定");
		put("FEATURE_nouucp","@ の左側(local part)に \"!\" がついたアドレスが、転送を許可されたシステムから出されたものでない場合の制御");
		put("FEATURE_always_add_domain","ローカル配送されたメールにもローカルなホストのドメインを付加");
		put("FEATURE_virtusertable","/etc/mail/virtusertableを読むように指定");
		put("define_confPRIVACY_FLAGS","ｾｷｭﾘﾃｨ設定");
		put("define_confMAX_DAEMON_CHILDREN","同時接続セッション数の最大値を設定");
		put("define_confCONNECTION_RATE_THROTTLE","1秒間に接続可能な新規接続数制限");
		put("define_confTO_IDENT","IDENTによる認証設定");
	}};
    
	public static  final Map<String, String> HttpdMap =new HashMap<String, String>(){{ 
		put("ServerType","サーバーの動作方法を指定する。standaloneは80番ポートなどの単独モードでデーモンとして常駐した状態で動作することを意味する。");
		put("ServerRoot","Apacheが必要とする設定ファイル（httpd.conf、access.conf、srm.conf等）の場所を示す。");
		put("PidFile","ロックファイルの場所を示す。");
		put("ScoreBoardFile","Apacheのstatus（稼動状況）を記入するファイルの場所を示す。");
		put("Timeout","クライアントとの間でデータパケットをやり取りする際に待機する最長の時間を秒単位で設定する。");
		put("KeepAlive","Webブラウザとの間で持続的な接続を維持するかどうかを指定する。");
		put("MaxKeepAliveRequests","1回の接続で処理できる要求の数を指定する。可能な限り大きな値を指定しておく。但し、KeepAliveをOnにしていないと無効。");
		put("KeepAliveTimeout","1つの要求が完了してから、接続を中断せずに次の新しい要求を待つ時間を秒単位で指定する。");
		put("MinSpareServers","最小の子サーバーの数を指定する。");
		put("MaxSpareServers","最大の子サーバーの数を指定する。");
		put("StartServers","起動時に作成される子サーバーの数を指定する。");
		put("MaxClients","同時に受け付けることができるクライアントの数を設定する。上限は256まで指定可能であるが、特に理由がない限りこのままでよい。");
		put("MaxRequestsPerChild","StartServer項目と同様、特に理由がない限り変更の必要はない。0は無制限であることを意味する。");
		put("LoadModule","DSOモードで読み込むモジュールを指定する。");
		put("AddModule","");
		put("Port","HTTPの標準ポート80番を指定する。");
		put("User","デフォルトのユーザー指定はnobodyとなっているが、ここではapacheという低い権限の専用のユーザー、グループを作成した上で動作させる。");
		put("Group","デフォルトのユーザー指定はnobodyとなっているが、ここではapacheという低い権限の専用のユーザー、グループを作成した上で動作させる。");
		put("ServerAdmin","サーバー管理者のメールアドレスを記入する。");
		put("ServerName","インターネットサーバーホスト名を指定する。ここに指定するものは、DNS（ドメインネームサーバー）に登録されている必要がある。");
		put("DocumentRoot","公開する文書が置かれている場所やURLをファイル名に変換する方法を指定する。");
		put("Directory","");
		put("Options","制御オプションの追加・変更・削除");
		put("AllowOverride","アクセス制御ファイルによるオーバーライドの許可");
		put("Order","");
		put("Allow","");
		put("IfModule","");
		put("UserDir","");
		put("AddHandler","");
		put("DirectoryIndex","デフォルトのファイル");
		put("AccessFileName","アクセス制御ファイルの名前を指定する。");
		put("UseCanonicalName","リクエストされたサーバ名とポートを検索するときにサーバー参照名（SERVER_NAME）などに別名を使えるようにする。");
		put("TypesConfig","");
		put("MIMEMagicFile","MIMEモジュールが提供するMIMEタイプを指定したファイルのパスを指定する。");
		put("HostnameLookups","IPアドレスに対応するホスト名を調べるためにDNSを参照するかどうかを設定する。");
		put("ErrorLog","エラーのあったリクエストを記録するエラーログファイルのパスを指定する。");
		put("LogLevel","エラーのあったリクエストを記録するエラーログファイルのレベルを指定する。");
		put("LogFormat","サーバーへのリクエストを記録するアクセスログのパスを指定する");
		put("CustomLog","サーバーへのリクエストを記録するアクセスログのパスを指定する");
		put("ServerSignature","サーバーが自動生成するページにApacheのバージョンと仮想ホストの名前を表示することを指定する");
		put("Alias","");
		put("ScriptAlias","");
		put("IndexOptions","Apacheで使用するアイコン画像の種類を指定する。");
		put("AddIconByEncoding","Apacheで使用するアイコン画像の種類を指定する。");
		put("AddIconByType","Apacheで使用するアイコン画像の種類を指定する。");
		put("AddIcon","Apacheで使用するアイコン画像の種類を指定する。");
		put("DefaultIcon","Apacheで使用するアイコン画像の種類を指定する。");
		put("ReadmeName","");
		put("HeaderName","");
		put("AddEncoding","ファイルがエンコードされていることを示す拡張子をApacheに指定する。");
		put("AddLanguage","サーバーに登録されている特定のコンテンツ言語や文字セットに対応するファイル名拡張子を割り当てる。");
		put("AddCharset","");
		put("AddType","AddTypeにより、新しいファイルタイプをMIMEに追加する。");
		put("BrowserMatch","BrowserMatchでは、指定した正規表現にマッチするパターンを持つ環境変数を設定する。");
		put("CacheNegotiatedDocs","コンテンツネゴシエーション（クライアントから送られてきた情報に基づいてサーバーが適切なコンテンツを返すこと）の結果返したドキュメントをキャッシュするかどうかを指定する。");
		put("DefaultType","ディレクトリに含まれているファイルのデフォルトのMIMEタイプを指定する。");
		put("AddHandler","この指定により、CGIスクリプトは拡張子.cgiのものだけが実行されるようになる。");
		put("mod_dir.c","ファイル名を何も指定せずにアクセスした際に表示させるファイル名及び優先順位を指定する");
		put("Require","任意のIPからのアクセスを許可する");
	}};
	
	public static  final Map<String, List<String>> HttpdDefaultMap =new HashMap<String, List<String>>(){{ 
		put("LoadModule",new ArrayList<String>() {{
			add("mod_access.so");
			add("mod_auth.so");
			add("mod_auth_anon.so");
			add("mod_auth_dbm.so");
			add("mod_auth_digest.so");
			add("mod_include.so");
			add("mod_log_config.so");
			add("mod_env.so");
			add("mod_mime_magic.so");
			add("mod_cern_meta.so");
			add("mod_expires.so");
			add("mod_headers.so");
			add("mod_usertrack.so");
			add("mod_unique_id.so");
			add("mod_setenvif.so");
			add("mod_mime.so");
			add("mod_dav.so");
			add("mod_status.so");
			add("mod_autoindex.so");
			add("mod_asis.so");
			add("mod_info.so");
			add("mod_dav_fs.so");
			add("mod_vhost_alias.so");
			add("mod_negotiation.so");
			add("mod_dir.so");
			add("mod_imap.so");
			add("mod_actions.so");
			add("mod_speling.so");
			add("mod_userdir.so");
			add("mod_alias.so");
			add("mod_rewrite.so");
			add("mod_proxy.so");
			add("mod_proxy_ftp.so");
			add("mod_proxy_http.so");
			add("mod_proxy_connect.so");
		}});
		put("AddModule",new ArrayList<String>() {{
			add("mod_access.c");
			add("mod_auth.c");
			add("mod_auth_anon.c");
			add("mod_auth_dbm.c");
			add("mod_auth_digest.c");
			add("mod_include.c");
			add("mod_log_config.c");
			add("mod_env.c");
			add("mod_mime_magic.c");
			add("mod_cern_meta.c");
			add("mod_expires.c");
			add("mod_headers.c");
			add("mod_usertrack.c");
			add("mod_unique_id.c");
			add("mod_setenvif.c");
			add("mod_mime.c");
			add("mod_dav.c");
			add("mod_status.c");
			add("mod_autoindex.c");
			add("mod_asis.c");
			add("mod_info.c");
			add("mod_dav_fs.c");
			add("mod_vhost_alias.c");
			add("mod_negotiation.c");
			add("mod_dir.c");
			add("mod_imap.c");
			add("mod_actions.c");
			add("mod_speling.c");
			add("mod_userdir.c");
			add("mod_alias.c");
			add("mod_rewrite.c");
			add("mod_proxy.c");
			add("mod_proxy_ftp.c");
			add("mod_proxy_http.c");
			add("mod_proxy_connect.c");
		}});
		put("AddIconByType",new ArrayList<String>() {{
			add("(TXT,/icons/text.gif) text/*");
			add("(IMG,/icons/image2.gif) image/*");
			add("(SND,/icons/sound2.gif) audio/*");
			add("(VID,/icons/movie.gif) video/*");
		}});
		put("AddIcon",new ArrayList<String>() {{
			add("/icons/binary.gif .bin .exe");
			add("/icons/binhex.gif .hqx");
			add("/icons/tar.gif .tar");
			add("/icons/world2.gif .wrl .wrl.gz .vrml .vrm .iv");
			add("/icons/compressed.gif .Z .z .tgz .gz .zip");
			add("/icons/a.gif .ps .ai .eps");
			add("/icons/layout.gif .html .shtml .htm .pdf");
			add("/icons/text.gif .txt");
			add("/icons/c.gif .c");
			add("/icons/p.gif .pl .py");
			add("/icons/f.gif .for");
			add("/icons/dvi.gif .dvi");
			add("/icons/uuencoded.gif .uu");
			add("/icons/script.gif .conf .sh .shar .csh .ksh .tcl");
			add("/icons/tex.gif .tex");
			add("/icons/bomb.gif core");
			add("/icons/back.gif ..");
			add("/icons/hand.right.gif README");
			add("/icons/folder.gif ^^DIRECTORY^^");
			add("/icons/blank.gif ^^BLANKICON^^");
		}});
		put("AddCharset",new ArrayList<String>() {{
			add("ISO-8859-1  .iso8859-1  .latin1");
			add("ISO-8859-2  .iso8859-2  .latin2 .cen");
			add("ISO-8859-3  .iso8859-3  .latin3");
			add("ISO-8859-4  .iso8859-4  .latin4");
			add("ISO-8859-5  .iso8859-5  .latin5 .cyr .iso-ru");
			add("ISO-8859-6  .iso8859-6  .latin6 .arb");
			add("ISO-8859-7  .iso8859-7  .latin7 .grk");
			add("ISO-8859-8  .iso8859-8  .latin8 .heb");
			add("ISO-8859-9  .iso8859-9  .latin9 .trk");
			add("ISO-2022-JP .iso2022-jp .jis");
			add("ISO-2022-KR .iso2022-kr .kis");
			add("ISO-2022-CN .iso2022-cn .cis");
			add("Big5        .Big5       .big5");
			add("WINDOWS-1251 .cp-1251   .win-1251");
			add("CP866       .cp866");
			add("KOI8-r      .koi8-r .koi8-ru");
			add("KOI8-ru     .koi8-uk .ua");
			add("ISO-10646-UCS-2 .ucs2");
			add("ISO-10646-UCS-4 .ucs4");
			add("UTF-8       .utf8");
			add("GB2312      .gb2312 .gb");
			add("utf-7       .utf7");
			add("utf-8       .utf8");
			add("big5        .big5 .b5");
			add("EUC-TW      .euc-tw");
			add("EUC-JP      .euc-jp");
			add("EUC-KR      .euc-kr");
			add("shift_jis   .sjis");
		}});
		put("BrowserMatch",new ArrayList<String>() {{
			add("\"Mozilla/2\" nokeepalive");
			add("\"MSIE 4\\.0b2;\" nokeepalive downgrade-1.0 force-response-1.0");
			add("\"RealPlayer 4\\.0\" force-response-1.0");
			add("\"Java/1\\.0\" force-response-1.0");
			add("\"JDK/1\\.0\" force-response-1.0");
			add("\"Microsoft Data Access Internet Publishing Provider\" redirect-carefully");
			add("\"^WebDrive\" redirect-carefully");
		}});
		put("AddLanguage",new ArrayList<String>() {{
			add("da .dk");
			add("nl .nl");
			add("en .en");
			add("et .et");
			add("fr .fr");
			add("de .de");
			add("he .he");
			add("el .el");
			add("it .it");
			add("ja .ja");
			add("pl .po");
			add("kr .kr");
			add("pt .pt");
			add("nn .nn");
			add("no .no");
			add("pt-br .pt-br");
			add("ltz .ltz");
			add("ca .ca");
			add("es .es");
			add("sv .se");
			add("cz .cz");
			add("ru .ru");
			add("tw .tw");
			add("zh-tw .tw");
			add("hr .hr");
		}});
		put("LogFormat",new ArrayList<String>() {{	add("\"%h %l %u %t \\\"%r\\\" %>s %b \\\"%{Referer}i\\\" \\\"%{User-Agent}i\\\"\" combined");
					add("\"%h %l %u %t \\\"%r\\\" %>s %b\" common");
					add("\"%{Referer}i -> %U\" referer");
					add("\"%{User-agent}i\" agent");
					}});
		put("User",new ArrayList<String>() {{	add("nobody");   }});
		put("Group",new ArrayList<String>() {{	add("nobody");   }});
		put("ServerAdmin",new ArrayList<String>() {{	add("root@localhost");   }});
		put("IndexOptions",new ArrayList<String>() {{	add("FancyIndexing VersionSort NameWidth=*");   }});
		put("addIconByEncoding",new ArrayList<String>() {{	add("(CMP,/icons/compressed.gif) x-compress x-gzip");   }});
		put("CustomLog",new ArrayList<String>() {{	add("logs/access_log combined");   }});
		put("ServerSignature",new ArrayList<String>() {{	add("On");   }});
		put("HostnameLookups",new ArrayList<String>() {{	add("Off");   }});
		put("ErrorLog",new ArrayList<String>() {{	add("logs/error_log");   }});
		put("LogLevel",new ArrayList<String>() {{	add("warn");   }});
		put("Timeout",new ArrayList<String>() {{	add("300");   }});
		put("KeepAlive",new ArrayList<String>() {{	add("Off");   }});
		put("MaxKeepAliveRequests",new ArrayList<String>() {{	add("100");   }});
		put("KeepAliveTimeout",new ArrayList<String>() {{	add("15");   }});
		put("StartServers",new ArrayList<String>() {{	add("8");   }});
		put("MinSpareServers",new ArrayList<String>() {{	add("5");   }});
		put("MaxSpareServers",new ArrayList<String>() {{	add("20");   }});
		put("MaxClients",new ArrayList<String>() {{	add("150");   }});
		put("MaxRequestsPerChild",new ArrayList<String>() {{	add("1000");   }});
		put("StartServers",new ArrayList<String>() {{	add("2");   }});
		put("MaxClients",new ArrayList<String>() {{	add("150");   }});
		put("MinSpareThreads",new ArrayList<String>() {{	add("25");   }});
		put("MaxSpareThreads",new ArrayList<String>() {{	add("75");   }});
		put("ThreadsPerChild",new ArrayList<String>() {{	add("25");   }});
		put("MaxRequestsPerChild",new ArrayList<String>() {{	add("0");   }});
		put("NumServers",new ArrayList<String>() {{	add("5");   }});
		put("StartThreads",new ArrayList<String>() {{	add("5");   }});
		put("MinSpareThreads",new ArrayList<String>() {{	add("5");   }});
		put("MaxSpareThreads",new ArrayList<String>() {{	add("10");   }});
		put("MaxThreadsPerChild",new ArrayList<String>() {{	add("20");   }});
		put("MaxRequestsPerChild",new ArrayList<String>() {{	add("0");   }});
		put("Listen",new ArrayList<String>() {{	add("80");   }});
		put("Port",new ArrayList<String>() {{	add("80");   }});
		put("UseCanonicalName",new ArrayList<String>() {{	add("Off");   }});
		put("DocumentRoot",new ArrayList<String>() {{	add("/var/www/html");   }});
	}};
	public static  final Map<String, List<String>> OutPutHeadMap =new HashMap<String, List<String>>(){{ 
		put("httpd",new ArrayList<String>() {{
			add("プロパティ");
			add("設定値/プロパティ２");
			add("設定値２");
			add("デフォルト");
			add("説明");
		}});
		put("passwd",new ArrayList<String>() {{
			add("ユーザ名");
			add("コメント");
			add("ユーザID");
			add("グループID");
			add("ホームDIR");
			add("ログインShell");
		}});
		put("group",new ArrayList<String>() {{
			add("グループ名");
			add("グループID");
			add("グループユーザ");
		}});
		put("cron",new ArrayList<String>() {{
			add("ユーザ");
			add("設定値");
		}});
		put("auth",new ArrayList<String>() {{
			add("ユーザ");
			add("設定値");
		}});
		put("profile",new ArrayList<String>() {{
			add("ユーザ");
			add("設定値");
		}});
		put("samba",new ArrayList<String>() {{
			add("プロパティ");
			add("設定値");
			add("説明");
		}});
		put("oracle",new ArrayList<String>() {{
			add("ネット・サービス名");
			add("設定値");
		}});
	}};

}
