import com.telecom.FileSystemAccessor;
import java.io.IOException;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public class MP3Cutter extends MIDlet
  implements CommandListener
{
  private Display mydisplay;
  private List myList;
  private Command exitMidlet = new Command("exit", 1, 2);
  private Command back = new Command("back", 7, 1);
  private Command index = new Command("back?", 7, 1);
  private Command okey = new Command("ok", 1, 2);
  private Command cutscr = new Command("select", 1, 2);
  private Command cut = new Command("ok", 1, 2);
  private Command aboutHelp = new Command("about & help", 1, 2);
  
  private Image anyfile;
  private Image audio;
  private Image disk;
  private Image folder;
  Image[] icons = null;
  private TextField tBitrate;
  private TextField tBegin;
  private TextField tEnd;
  private TextField tNameFile;
  String[] files;
  String nameT = "";

  public MP3Cutter()
  {
    try
    {
      this.anyfile = Image.createImage("/icons/anyfile.png");
      this.audio = Image.createImage("/icons/audio.png");
      this.disk = Image.createImage("/icons/disk.png");
      this.folder = Image.createImage("/icons/folder.png");
    }
    catch (IOException localIOException)
    {
      System.out.println("ex = " + localIOException);
    }
    this.mydisplay = Display.getDisplay(this);
  }

  public void pauseApp()
  {
  }

  public void destroyApp(boolean paramBoolean)
  {
  }

  public void startApp()
  {
    System.out.println("startApp");
    FileSystemAccessor localFileSystemAccessor = new FileSystemAccessor(this.nameT);
    String str = this.nameT;
    int i;
    if (this.nameT.equals(""))
    {
      str = "MP3Cutter";
      this.files = FileSystemAccessor.listRoots();
      this.icons = new Image[this.files.length];
      for (i = 0; i < this.files.length; i++)
        this.icons[i] = this.disk;
    }
    else
    {
            try {
                this.files = localFileSystemAccessor.list();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
      this.icons = new Image[this.files.length];
      for (i = 0; i < this.files.length; i++)
      {
        this.files[i] = this.files[i].substring(this.nameT.length());
        this.icons[i] = this.anyfile;
        if (this.files[i].substring(this.files[i].length() - 1, this.files[i].length()).equals("/"))
          this.icons[i] = this.folder;
        else if ((this.files[i].length() > 4) && ((this.files[i].substring(this.files[i].length() - 4, this.files[i].length()).equals(".mp3")) || (this.files[i].substring(this.files[i].length() - 4, this.files[i].length()).equals(".MP3"))))
          this.icons[i] = this.audio;
      }
    }
    this.myList = new List(str, List.IMPLICIT, this.files, this.icons);
    
    if (this.nameT.equals(""))
      this.myList.addCommand(this.exitMidlet);
    else
      this.myList.addCommand(this.back);
      this.myList.addCommand(this.aboutHelp);
    
    this.myList.addCommand(this.okey);
    this.myList.addCommand(this.cutscr);
    this.myList.addCommand(this.exitMidlet);
    this.myList.setSelectCommand(this.okey);
    this.myList.setCommandListener(this);
    this.mydisplay.setCurrent(this.myList);
  }

  public void commandAction(Command paramCommand, Displayable paramDisplayable)
  {
      if(paramCommand== this.aboutHelp){
          Display.getDisplay(this).setCurrent(new Alert("About & help", "MP3 Cutter Pro \n  ver 1.1 deverloper name: vu van tuong\n\n You can select music files mp3 and music selection cut desired time!  ", null, AlertType.INFO));
          
      } else 
    if (paramCommand == this.exitMidlet)
    {
      destroyApp(false);
      notifyDestroyed();
    }
    if (paramCommand == this.okey)
    {
      String str1 = this.files[this.myList.getSelectedIndex()];
      str1 = str1.substring(str1.length() - 1, str1.length());
      if (str1.equals("/"))
      {
        this.nameT += this.files[this.myList.getSelectedIndex()];
        System.out.println("OK: nameT = " + this.nameT);
        startApp();
      }
    }
    String str2;
    if (paramCommand == this.back)
    {
      if (this.nameT.length() < 4)
        this.nameT = "";
      else
        for (int i = 2; i < this.nameT.length(); i++)
        {
          str2 = this.nameT.substring(this.nameT.length() - i, this.nameT.length() - i + 1);
          if (str2.equals("/"))
            this.nameT = this.nameT.substring(0, this.nameT.length() - i + 1);
        }
      System.out.println("BACK: nameT = " + this.nameT);
      startApp();
    }
    Object localObject;
    if (paramCommand == this.cutscr)
    {
      localObject = new Form("MP3 Cutter v 1.1");
      this.tNameFile = new TextField("files: ", this.nameT + this.files[this.myList.getSelectedIndex()], 255, 0);
      this.tBitrate = new TextField("rate quality (kb/s)", "128", 3, 2);
      this.tBegin = new TextField("start time (s)", "0", 255, 2);
      this.tEnd = new TextField("end time (s)", "10", 255, 2);
      ((Form)localObject).append(this.tNameFile);
      ((Form)localObject).append(this.tBitrate);
      ((Form)localObject).append(this.tBegin);
      ((Form)localObject).append(this.tEnd);
      ((Form)localObject).addCommand(this.index);
      ((Form)localObject).addCommand(this.cut);
      ((Form)localObject).setCommandListener(this);
      this.mydisplay.setCurrent((Displayable)localObject);
    }
    if (paramCommand == this.index)
      startApp();
    if (paramCommand == this.cut)
    {
      str2 = this.nameT + this.files[this.myList.getSelectedIndex()];
      localObject = str2.substring(0, str2.length() - 4) + "_cut.mp3";
      System.out.println((String)localObject);
      int j = Integer.parseInt(this.tBitrate.getString());
      int k = j / 8 * 1024 * Integer.parseInt(this.tBegin.getString());
      int m = j / 8 * 1024 * Integer.parseInt(this.tEnd.getString());
      Cut localCut = new Cut();
      Cut.copyfile(str2, (String)localObject, k, m - k, 10000);
      startApp();
    }
  }
}

/* Location:           I:\Users\Wall\Desktop\app\app\Cat_Nhac.jar
 * Qualified Name:     MP3Cutter
 * JD-Core Version:    0.6.2
 */