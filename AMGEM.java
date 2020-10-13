import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/* 
 * Program:     Among Us gameHostOptions Editor & Manager (AMGEM)
 * Author:      ~ ♀ Yuri Bacon ♀ ~
 * Description: A program that makes editing Among Us host settings easy, so not normally availabe option values are available to anyone who wants to host custom games better & easier.
 * Date:        11 Oct 2020
 * 
 * Assume sus & never trust. Also assume they/them pronouns as default, that'd be great :)
 */
class AMGEM
{
  public static void main(String[] args)
  {
    //Creating a Scanner and String input for taking input from the user
    //Also creating boolean quit, which when true, causes the main loop to break and the program to exit, but repeats the main menu otherwise
    Scanner in = new Scanner(System.in);
    String input = "";
    boolean quit = false;
    
    //Creating the File variables for the gametypes & save data folders
    File saveDataFolder;
    File gametypesFolder = new File("." + File.separator + "Gametypes");
    
    if(args.length == 2 && args[0].equals("-ap"))
      saveDataFolder = new File(args[1]);
    else
      saveDataFolder = new File("C:" + File.separator + "Users" + File.separator + System.getProperty("user.name") + File.separator + "AppData" + File.separator + "LocalLow" + File.separator + "Innersloth" + File.separator + "AmongUs");
    
    //If this program isn't running on Windows, and wasn't given any arguments, we promt the user for their H2V directory.
    //If we do get arguments, we use the path provided.
    //This should only apply when running on Linux or Mac.
    if(System.getProperty("os.name").indexOf("Windows") == -1 && args.length != 2)
    {
      boolean promptForPath = true;
      System.out.println("AMGEM has detected that it is not running under Windows.\n" + 
                         "For future reference, you can pass the argument \"-ap\" for the absolute path to the Among Us save data folder. " + 
                         "Please enter the path to your Among Us save data folder now (leave blank to default to /home/<current user>/.local/share/Steam/steamapps/compatdata/945360/pfx/drive_c/users/steamuser/AppData/LocalLow/Innersloth/Among Us):");
      input = in.nextLine();
      System.out.println();
      
      if(input.equals(""))
        saveDataFolder = new File(File.separator + "home" + File.separator + System.getProperty("user.name") + File.separator + ".local" + File.separator + "share" + File.separator + "Steam" + File.separator + "steamapps" + File.separator + "compatdata" + File.separator + "945360" + File.separator + "pfx" + File.separator + "drive_c" + File.separator + "users" + File.separator + "steamuser" + File.separator + "AppData" + File.separator + "LocalLow" + File.separator + "Innersloth" + File.separator + "Among Us");
      else
        saveDataFolder = new File(input);
      
      System.out.println();
    }
    
    //ArrayLists to hold all the gametypes
    ArrayList<String> gametypes = new ArrayList<String>();
    
    //Testing if the gametype folder exists and populate the gametypes found in the gametype ArrayLists, otherwise just create it
    if(gametypesFolder.isDirectory())
    {
      System.out.print("Loading gametype list...");
      
      gametypes = loadMetadata(gametypesFolder.getPath());
      
      System.out.println(" Done.");
    }else
    {
      gametypesFolder.mkdirs();
    }
    
    //One time startup welcome message~!
    System.out.println("\nWelcome to Among Us gameHostOptions Editor & Manager~!!");
    
    do
    {
      //Promt user to pick one of the options, and then takes in that input.
      //Prints extra blank line after taking input to put white space between blocks of console output for aesthetics, 'cause Windows CMD is ugly. Well, all terminals are kinda ugly, but Windows especially.
      System.out.print("Please select a option:\n\nN)ew Gametype\nE)dit Gametype\nS)wap Gametype\nH)elp\nQ)uit\n");
      input = in.nextLine().toLowerCase();
      System.out.println();
      
      //Processing the user input string, and executing the corresponding code blocks. Loops when any are finished
      if(input.equals("q") || input.equals("quit"))
      {
        //Tells user to have fun with more Among Us, and sets quit to true so the program will exit
        System.out.println("Leaving your task so fast? Thats pretty sus\n\nEnjoy custom Among Us~!");
        quit = true;
      }else if(input.equals("e") || input.equals("edit"))
      {
        //Creates boolean optionSelected so we can keep looping the user input promt until we have valid input
        boolean optionSelected = false;
        
        //Create boolean inputIsNumber, which is used both to make sure the user input is a number before we try to parse it as an integer, and that the input number is within the valid range of options.
        //We assume its true, then check if its false. If it passes our check, we don't repeat the input loop.
        boolean inputIsNumber = true;
        
        do
        {
          //set inputIsNumber back to true in case this is the second or on repeat of this loop
          inputIsNumber = true;
          
          //Prompt user for which gametype they'd like to edit, listing each gametype in the ArrayList as its own option, and adding cancel on to the end
          System.out.println("Please select which gametype you'd like to edit (make sure you close Among Us first!):");
          for(int i = 0; i < gametypes.size(); i++)
            System.out.println(i + ") " + gametypes.get(i));
          System.out.println(gametypes.size() + ") Cancel");
          input = in.nextLine();
          System.out.println();
          
          //we check each character in the string to see if any of them are a number. If any character is found that isn't a number 0-9, we set inputIsNumber to false to make this input loop repeat
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0'))
              inputIsNumber = false;
          
          //if the string test above failed, we prompt the user to enter a vaild number. If it passed, we check to make sure it is in range. If it isn't we set inputIsNumber to false to make the input loop repeat, and promt them to enter a valid number.
          if(inputIsNumber)
          {
            if(!(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= gametypes.size()))
            {
              System.out.println("Input does not correspond to a gametype listed. Please enter a number listed below.");
              inputIsNumber = false;
            }
          }else
            System.out.println("Input is not a number. Please enter a numbered option listed for the desired gametype to edit.");
        }while(!inputIsNumber);
        
        //Haha, INTput? Get it? I'll show myself out.
        //Create an int of the input number, now that we have confirmed it to be valid.
        int intPut = Integer.parseInt(input);
        
        if(intPut >= 0 && intPut < gametypes.size())
        {
          //Hand the gametype off to the gametype edit method
          editGametype(gametypesFolder.getPath() + File.separator + gametypes.get(intPut), gametypes.get(intPut));
        }
        
        //Recalling the loadMetadata() method so that our ArrayLists reflect the changes we just made
        System.out.print("Refreshing gametype list...");
        gametypes = loadMetadata(gametypesFolder.getPath());
        System.out.println(" Done.");
      }else if(input.equals("n") || input.equals("new"))
      {
        //variable to know if the name entered hasn't been used yet
        boolean validName = true;
        
        do
        {
          //Reset the validName variable, prompt, and take input
          validName = true;
          System.out.print("Enter your gametype name: ");
          input = in.nextLine();
          
          //If the name isn't emtpy, we check to see if there is any other gametype with the same name. If not it is valid, otherwise it isn't.
          if(!input.equals(""))
          {
            for(int i = 0; i < gametypes.size(); i++)
            {
              if(input.equals(gametypes.get(i)))
              {
                System.out.println("Name already in use.");
                validName = false;
              }
            }
          }else
          {
            System.out.println("Enter a name.");
            validName = false;
          }
        }while(!validName);
        
        //We add the name to the gametypes ArrayList, and make the corresponding directory for it
        gametypes.add(input);
        File newGametype = new File(gametypesFolder.getPath() + File.separator + input);
        newGametype.mkdirs();
        
        //We take the current host options and swap them into the new gametype folder, then send it off to the edit method
        swapGametype(saveDataFolder.getPath(), newGametype.getPath() + File.separator + "gameHostOptions");
        editGametype(newGametype.getPath(), input);
        
        //Recalling the loadMetadata() method so that our ArrayLists reflect the changes we just made
        System.out.print("Refreshing gametype list...");
        gametypes = loadMetadata(gametypesFolder.getPath());
        System.out.println(" Done.");
      }else if(input.equals("h") || input.equals("help"))
      {
        //help message :P
        System.out.println("AMGEM is a program to edit the file \"gameHostOptions\", which is where Among Us stores the game settings you last used when hosting a lobby.\nNamely, it allows you to edit various varables to values not normally allowed by the game, like:\n ~ Very short/long imposter kill times\n ~ Absurdly slow/fast movement speeds\n ~ Absurdly low/high crewmate/impostervision\n ~ Infinite voting time\n ~ Negative discussion time (votes end instantly)\n ~ And others\nThese options allow more options and flexibility when trying to play \"custom games\" like Hide & Seek or anything else your creativity can think up of.\nAMGEM also allows you to save your rules as \"gametypes\", so you can have the settings for many different custom games on hand and ready to use at any time, just requiring you to swap them in.\nNew Gametype will take your current gameHostOptions, save it as a gametype, and then let you edit it further.\nEdit gametype will take a gametype you already created and allow you to edit further.\nSwap gametype will take one of your gametypes and load them into your Among Us save data. Make sure to close Among Us before swapping in a gametype!");
      }else if(input.equals("s") || input.equals("swap"))
      {
        //Creates boolean optionSelected so we can keep looping the user input promt until we have valid input
        boolean optionSelected = false;
        
        //Create boolean inputIsNumber, which is used both to make sure the user input is a number before we try to parse it as an integer, and that the input number is within the valid range of options.
        //We assume its true, then check if its false. If it passes our check, we don't repeat the input loop.
        boolean inputIsNumber = true;
        
        do
        {
          //set inputIsNumber back to true in case this is the second or on repeat of this loop
          inputIsNumber = true;
          
          //Prompt user for which gametype they'd like to swap in, listing each gametype in the ArrayList as its own option, and adding cancel on to the end
          System.out.println("Please select which gametype you'd like to Swap in (make sure you close Among Us first!):");
          for(int i = 0; i < gametypes.size(); i++)
            System.out.println(i + ") " + gametypes.get(i));
          System.out.println(gametypes.size() + ") Cancel");
          input = in.nextLine();
          System.out.println();
          
          //we check each character in the string to see if any of them are a number. If any character is found that isn't a number 0-9, we set inputIsNumber to false to make this input loop repeat
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0'))
              inputIsNumber = false;
          
          //if the string test above failed, we prompt the user to enter a vaild number. If it passed, we check to make sure it is in range. If it isn't we set inputIsNumber to false to make the input loop repeat, and promt them to enter a valid number.
          if(inputIsNumber)
          {
            if(!(Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= gametypes.size()))
            {
              System.out.println("Input does not correspond to a gametype listed. Please enter a number listed below.");
              inputIsNumber = false;
            }
          }else
            System.out.println("Input is not a number. Please enter a numbered option listed for the desired gametype to edit.");
        }while(!inputIsNumber);
        
        //Haha, INTput? Get it? I'll show myself out.
        //Create an int of the input number, now that we have confirmed it to be valid.
        int intPut = Integer.parseInt(input);
        
        if(intPut >= 0 && intPut < gametypes.size())
        {
          //Hand the gametype off to the gametype edit method
          swapGametype(gametypesFolder.getPath() + File.separator + gametypes.get(intPut), saveDataFolder.getPath() + File.separator + "gameHostOptions");
          System.out.println("Swapped in " + gametypes.get(intPut) + "~!");
        }
        
        //Recalling the loadMetadata() method so that our ArrayLists reflect the changes we just made
        System.out.print("Refreshing gametype list...");
        gametypes = loadMetadata(gametypesFolder.getPath());
        System.out.println(" Done.");
      }else
        System.out.println("Sorry, couldn't process what option you have selected.");
    }while(!quit);
  }
  
  //String gametypeFolderPath is the path of the gametypes folder we want to load all the metadata from
  private static ArrayList<String> loadMetadata(String gametypeFolderPath)
  {
    //Makes a File object for the gametype folder we are loading metadata for, and an ArrayList we are putting the metadata in to eventually return
    File gametypesFolder = new File(gametypeFolderPath);
    ArrayList<String> gametypes = new ArrayList<String>();
    
    //Gets an array of all the files and folders in the gametype folder
    String[] folders = gametypesFolder.list();
    
    //Iterating through every entry and test if it is a folder. If it is, we look for a gameHostOptions file.
    //If we can't find it, we ignore the folder. If we find it, we add it to the gametype array.
    for(int i = 0; i < folders.length; i++)
    {
      //We create a File object for the current file or folder we are dealing with, and check if it is a directory
      File folder = new File(gametypesFolder.getPath() + File.separator + folders[i]);
      
      //Variable to be sure this folder is a gametype folder
      boolean hasGametype = false;
      
      if(folder.isDirectory())
      {
        //An array with the current file we are checking for.
        String[] files = folder.list();
        
        for(int r = 0; r < files.length; r++)
        {
          if(files[r].equals("gameHostOptions"))
              hasGametype = true;
        }
      }
      
      //If we find a valid gametype in this folder, we add it to the ArrayList
      if(hasGametype)
      {
        gametypes.add(folders[i]);
      }
    }
    
    //now that loading metadata is all done, we return the ArrayList to the main method
    return gametypes;
  }
  
  //String gametypeFolderPath should be the folder of the gametype to be edited
  //String gametypeName is just the name of the gametype
  private static void editGametype(String gametypeFolderPath, String gametypeName)
  {
    //File object for the gameHostOptions file
    File gameHostOptions = new File(gametypeFolderPath + File.separator + "gameHostOptions");
    
    //String for user input
    String input = "";
    Scanner in = new Scanner(System.in);
    
    //Now we load the gametype in as ints
    //We create an int array to load the gametype into
    int[] gametype = new int[44];
    
    //we load in the raw byte values of the gametype file. 
    try
    {
      int next = -2;
      FileInputStream fileStream = new FileInputStream(gameHostOptions);
      for(int i = 0; i < gametype.length; i++)
      {
        next = fileStream.read();
        if(next >= 0)
          gametype[i] = next;
      }
      fileStream.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    
    //Now we create an actual byte array to store the gametype in. 
    byte[] gametypeBytes = new byte[44];
    
    //We fill in each byte in the array with the byte value of the corresponding int from the int array
    for(int i = 0; i < gametypeBytes.length; i++)
      gametypeBytes[i] = Integer.valueOf(gametype[i]).byteValue();
    
    //We check to make sure that the first byte in the file is 3. If it isn't, we correct it and warn the user that the gametype might not be valid, but procced with zero caution like the lazy programer I am.
    if(gametypeBytes[0] != 3)
    {
      System.err.println("Error! The first byte in the gametype file should be 3, but it wasn't. We'll correct this and try to procced normally, but this gametype might be corrupt or invalid.");
      gametypeBytes[0] = 3;
      gametype[0] = 3;
    }
    
    //We'll be needing this for any byte/int to float conversion and other data type conversions, fun ahoy!
    ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
    
    //All of the gametype settings, listed in the order they appear in game (based on online lobby order of apperance)
    int map = gametype[6];
    int imposterCount = gametype[30];
    int maxPlayers = gametype[1];
    
    int recommendedSettings = gametype[40];
    int confirmEjects = gametype[42];
    int emergencyMeetings = byteBuffer.put(0, gametypeBytes[26]).put(1, gametypeBytes[27]).put(2, gametypeBytes[28]).put(3, gametypeBytes[29]).getInt(0);
    int emergencyCooldown = gametype[41];
    int discussionTime = byteBuffer.put(0, gametypeBytes[32]).put(1, gametypeBytes[33]).put(2, gametypeBytes[34]).put(3, gametypeBytes[35]).getInt(0);
    int votingTime = byteBuffer.put(0, gametypeBytes[36]).put(1, gametypeBytes[37]).put(2, gametypeBytes[38]).put(3, gametypeBytes[39]).getInt(0);
    float playerSpeed = byteBuffer.put(0, gametypeBytes[7]).put(1, gametypeBytes[8]).put(2, gametypeBytes[9]).put(3, gametypeBytes[10]).getFloat(0);
    float crewmateVision = byteBuffer.put(0, gametypeBytes[11]).put(1, gametypeBytes[12]).put(2, gametypeBytes[13]).put(3, gametypeBytes[14]).getFloat(0);
    float imposterVision = byteBuffer.put(0, gametypeBytes[15]).put(1, gametypeBytes[16]).put(2, gametypeBytes[17]).put(3, gametypeBytes[18]).getFloat(0);
    float killCooldown = byteBuffer.put(0, gametypeBytes[19]).put(1, gametypeBytes[20]).put(2, gametypeBytes[21]).put(3, gametypeBytes[22]).getFloat(0);
    int killDistance = gametype[31];
    int visualTasks = gametype[43];
    int commonTasks = gametype[23];
    int longTasks = gametype[24];
    int shortTasks = gametype[25];
    
    
    //Now we can start the editing!! We present the user with all the options and take their input.
    //I couldn't be bothered to actually comment all of this like I did for the main method.
    //Its tons of print() statements and nextLine() anyways :P There's tons of it there, but its not much to look at really. 
    boolean run = true;
    boolean save = true;
    
    while(run)
    {
      input = "";
      System.out.print("\nEditing " + gametypeName + "...\n\n 0) Recommended Settings: ");
      if(recommendedSettings == 1)
        System.out.println("On");
      else if(recommendedSettings == 0)
        System.out.println("Off");
      else
        System.out.println();
      System.out.print(" 1) Map: ");
      if(map == 0)
        System.out.println("The Skeld");
      else if(map == 1)
        System.out.println("Mira HQ");
      else if(map == 2)
        System.out.println("Polus");
      else
        System.out.println();
      System.out.print(" 2) # of Imposters: " + imposterCount + "\n 3) Confirm Ejects: ");
      if(confirmEjects == 1)
        System.out.println("On");
      else if(confirmEjects == 0)
        System.out.println("Off");
      else
        System.out.println();
      System.out.print(" 4) # of Emergency Meetings: " + emergencyMeetings + "\n 5) Emergency Cooldown: " + emergencyCooldown + " seconds\n 6) Discussion Time: " + discussionTime + " seconds\n 7) Voting Time: " + votingTime + " seconds\n 8) Player Speed: " + playerSpeed + "x\n 9) Crewmate Vision: " + crewmateVision + "x\n10) Imposter Vision: " + imposterVision + "x\n11) Kill Cooldown: " + killCooldown + " seconds\n12) Kill Distance: ");
      if(killDistance == 0)
        System.out.println("Short");
      else if(killDistance == 1)
        System.out.println("Medium");
      else if(killDistance == 2)
        System.out.println("Long");
      else
        System.out.println();
      System.out.print("13) Visual Tasks: ");
      if(visualTasks == 1)
        System.out.println("On");
      else if(visualTasks == 0)
        System.out.println("Off");
      else
        System.out.println();
      System.out.print("14) # of Common Tasks: " + commonTasks + "\n15) # of Long Tasks: " + longTasks + "\n16) # of Short Tasks: " + shortTasks + "\n17) Max Players: " + maxPlayers + "\nS)ave Gametype\nQ)uit Without Saving\n");
      input = in.nextLine().toLowerCase();
      
      if(input.equals("0"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Recommended Settings ticked:\n0) Off\n1) On");
          input = in.nextLine();
          
          if(input.equals("0"))
          {
            recommendedSettings = 0;
            promptInput = false;
          }else if(input.equals("1"))
          {
            recommendedSettings = 1;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("1"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Map:\n0) The Skeld\n1) Mira HQ\n2) Polus");
          input = in.nextLine();
          
          if(input.equals("0"))
          {
            map = 0;
            promptInput = false;
          }else if(input.equals("1"))
          {
            map = 1;
            promptInput = false;
          }else if(input.equals("2"))
          {
            map = 2;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("2"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("# of Imposters (1-3): ");
          input = in.nextLine();
          
          if(input.equals("3"))
          {
            imposterCount = 3;
            promptInput = false;
          }else if(input.equals("1"))
          {
            imposterCount = 1;
            promptInput = false;
          }else if(input.equals("2"))
          {
            imposterCount = 2;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("3"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Confirm Ejects:\n0) Off\n1) On");
          input = in.nextLine();
          
          if(input.equals("0"))
          {
            confirmEjects = 0;
            promptInput = false;
          }else if(input.equals("1"))
          {
            confirmEjects = 1;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("4"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Negative values disallow the crew from holding meetings.\n# of Emergency Meetings (-2,147,483,648 - 2,147,483,647): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            emergencyMeetings = Integer.parseInt(input);
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("5"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.print("Emergency Cooldown (0-255 seconds): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            int temp = Integer.parseInt(input);
            if(temp >= 0 && temp <= 255)
            {
              emergencyCooldown = temp;
              promptInput = false;
            }else
              System.out.println("Must be 0-255 seconds");
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("6"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: If this value is negative, the game will progress to voting time but add the amount of negative discussion time to the voting timer. With negative discussion time and positive voting time, this can result in meetings instantly moving to the post-vote stage.\nDiscussion time (-2,147,483,648 - 2,147,483,647 seconds): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            discussionTime = Integer.parseInt(input);
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("7"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Negative values are displayed as infinity in the lobby, in the vote screen the timer is completely gone.\nDiscussion time (-2,147,483,648 - 2,147,483,647 seconds): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            votingTime = Integer.parseInt(input);
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("8"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Negative values invert controls, although lag compensation expects regular controls which can result in other players teleporting a lot. Extremely low/high values result in no movement whatsoever.\nPlayer Speed (-99999.0 - 99999.0): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          if(input.length() < 10 && input.length() > 0)
          {
            if(!(input.charAt(0) == '1' || input.charAt(0) == '2' || input.charAt(0) == '3' || input.charAt(0) == '4' || input.charAt(0) == '5' || input.charAt(0) == '6' || input.charAt(0) == '7' || input.charAt(0) == '8' || input.charAt(0) == '9' || input.charAt(0) == '0' || input.charAt(0) == '.' || input.charAt(0) == '-'))
              inputIsNumber = false;
            
            for(int i = 1; i < input.length(); i++)
            {
              if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '.'))
              {
                inputIsNumber = false;
              }
            }
          }else
          {
            inputIsNumber = false;
          }
          
          boolean inputHasNumber = false;
          for(int i = 0; i < input.length(); i++)
          {
            if(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0')
            {
              inputHasNumber = true;
            }
          }
          
          boolean inputHasLessThan2Periods = true;
          if(input.indexOf(".") != -1)
          {
            if(input.substring(input.indexOf(".") + 1).indexOf(".") != -1)
            {
              inputHasLessThan2Periods = false;
            }
          }
          
          if(inputIsNumber && inputHasNumber && inputHasLessThan2Periods)
          {
            float temp = Float.parseFloat(input);
            if(temp < 99999.0 && temp > -99999)
            {
              playerSpeed = temp;
              promptInput = false;
            }
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("9"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Extremely high vision values make the user effectively blind minus very specific locations. Negative vision values function as normal although shadows are drawn with decreased accuracy. 0 vision results in being unable to see anything (vents still can be viewed through fog of war).\nCrewmate Vision (-99999.0 - 99999.0): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          if(input.length() < 10 && input.length() > 0)
          {
            if(!(input.charAt(0) == '1' || input.charAt(0) == '2' || input.charAt(0) == '3' || input.charAt(0) == '4' || input.charAt(0) == '5' || input.charAt(0) == '6' || input.charAt(0) == '7' || input.charAt(0) == '8' || input.charAt(0) == '9' || input.charAt(0) == '0' || input.charAt(0) == '.' || input.charAt(0) == '-'))
              inputIsNumber = false;
            
            for(int i = 1; i < input.length(); i++)
            {
              if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '.'))
              {
                inputIsNumber = false;
              }
            }
          }else
          {
            inputIsNumber = false;
          }
          
          boolean inputHasNumber = false;
          for(int i = 0; i < input.length(); i++)
          {
            if(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0')
            {
              inputHasNumber = true;
            }
          }
          
          boolean inputHasLessThan2Periods = true;
          if(input.indexOf(".") != -1)
          {
            if(input.substring(input.indexOf(".") + 1).indexOf(".") != -1)
            {
              inputHasLessThan2Periods = false;
            }
          }
          
          if(inputIsNumber && inputHasNumber && inputHasLessThan2Periods)
          {
            float temp = Float.parseFloat(input);
            if(temp < 99999.0 && temp > -99999)
            {
              crewmateVision = temp;
              promptInput = false;
            }
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("10"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Extremely high vision values make the user effectively blind minus very specific locations. Negative vision values function as normal although shadows are drawn with decreased accuracy. 0 vision results in being unable to see anything (vents still can be viewed through fog of war).\nImposter Vision (-99999.0 - 99999.0): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          if(input.length() < 10 && input.length() > 0)
          {
            if(!(input.charAt(0) == '1' || input.charAt(0) == '2' || input.charAt(0) == '3' || input.charAt(0) == '4' || input.charAt(0) == '5' || input.charAt(0) == '6' || input.charAt(0) == '7' || input.charAt(0) == '8' || input.charAt(0) == '9' || input.charAt(0) == '0' || input.charAt(0) == '.' || input.charAt(0) == '-'))
              inputIsNumber = false;
            
            for(int i = 1; i < input.length(); i++)
            {
              if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '.'))
              {
                inputIsNumber = false;
              }
            }
          }else
          {
            inputIsNumber = false;
          }
          
          boolean inputHasNumber = false;
          for(int i = 0; i < input.length(); i++)
          {
            if(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0')
            {
              inputHasNumber = true;
            }
          }
          
          boolean inputHasLessThan2Periods = true;
          if(input.indexOf(".") != -1)
          {
            if(input.substring(input.indexOf(".") + 1).indexOf(".") != -1)
            {
              inputHasLessThan2Periods = false;
            }
          }
          
          if(inputIsNumber && inputHasNumber && inputHasLessThan2Periods)
          {
            float temp = Float.parseFloat(input);
            if(temp < 99999.0 && temp > -99999)
            {
              imposterVision = temp;
              promptInput = false;
            }
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("11"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Note: Negative values bypass the the 10 second starting cooldown, and 0 makes the kill button disappear, but you can still kill with keyboard key Q.\nKill Cooldown (-99999.0 - 99999.0 seconds): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          if(input.length() < 10 && input.length() > 0)
          {
            if(!(input.charAt(0) == '1' || input.charAt(0) == '2' || input.charAt(0) == '3' || input.charAt(0) == '4' || input.charAt(0) == '5' || input.charAt(0) == '6' || input.charAt(0) == '7' || input.charAt(0) == '8' || input.charAt(0) == '9' || input.charAt(0) == '0' || input.charAt(0) == '.' || input.charAt(0) == '-'))
              inputIsNumber = false;
            
            for(int i = 1; i < input.length(); i++)
            {
              if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '.'))
              {
                inputIsNumber = false;
              }
            }
          }else
          {
            inputIsNumber = false;
          }
          
          boolean inputHasNumber = false;
          for(int i = 0; i < input.length(); i++)
          {
            if(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0')
            {
              inputHasNumber = true;
            }
          }
          
          boolean inputHasLessThan2Periods = true;
          if(input.indexOf(".") != -1)
          {
            if(input.substring(input.indexOf(".") + 1).indexOf(".") != -1)
            {
              inputHasLessThan2Periods = false;
            }
          }
          
          if(inputIsNumber && inputHasNumber && inputHasLessThan2Periods)
          {
            float temp = Float.parseFloat(input);
            if(temp < 99999.0 && temp > -99999)
            {
              killCooldown = temp;
              promptInput = false;
            }
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("12"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Kill Distance:\n0) Short\n1) Medium\n2) Long");
          input = in.nextLine();
          
          if(input.equals("0"))
          {
            killDistance = 0;
            promptInput = false;
          }else if(input.equals("1"))
          {
            killDistance = 1;
            promptInput = false;
          }else if(input.equals("2"))
          {
            killDistance = 2;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("13"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.println("Visual Tasks:\n0) Off\n1) On");
          input = in.nextLine();
          
          if(input.equals("0"))
          {
            visualTasks = 0;
            promptInput = false;
          }else if(input.equals("1"))
          {
            visualTasks = 1;
            promptInput = false;
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("14"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.print("Common Tasks (0-255): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            int temp = Integer.parseInt(input);
            if(temp >= 0 && temp <= 255)
            {
              commonTasks = temp;
              promptInput = false;
            }else
              System.out.println("Must be 0-255");
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("15"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.print("Long Tasks (0-255): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            int temp = Integer.parseInt(input);
            if(temp >= 0 && temp <= 255)
            {
              longTasks = temp;
              promptInput = false;
            }else
              System.out.println("Must be 0-255");
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("16"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.print("Short Tasks (0-255): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            int temp = Integer.parseInt(input);
            if(temp >= 0 && temp <= 255)
            {
              shortTasks = temp;
              promptInput = false;
            }else
              System.out.println("Must be 0-255");
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("17"))
      {
        boolean promptInput = true;
        
        while(promptInput)
        {
          promptInput = true;
          
          System.out.print("Note: Value of 0 will tell you that the game you attempted to join is full, and defaults your lobby language to portuguese. Only happens in online lobbies, local works fine. Value of 01 crashes the game when making an online lobby (local works fine). Values 2-10 work normally. Values above 10 in online lobbies results in error “Game options received were invalid” (local works). This interaction needs more testing with local play.\nMax Players (0-255): ");
          input = in.nextLine();
          
          boolean inputIsNumber = true;
          for(int i = 0; i < input.length(); i++)
            if(!(input.charAt(i) == '1' || input.charAt(i) == '2' || input.charAt(i) == '3' || input.charAt(i) == '4' || input.charAt(i) == '5' || input.charAt(i) == '6' || input.charAt(i) == '7' || input.charAt(i) == '8' || input.charAt(i) == '9' || input.charAt(i) == '0' || input.charAt(i) == '-'))
              inputIsNumber = false;
          
          if(inputIsNumber)
          {
            int temp = Integer.parseInt(input);
            if(temp >= 0 && temp <= 255)
            {
              maxPlayers = temp;
              promptInput = false;
            }else
              System.out.println("Must be 0-255");
          }else
            System.out.println("Sorry, could not process your selection.");
        }
      }else if(input.equals("s"))
      {
        run = false;
      }else if(input.equals("q"))
      {
        run = false;
        save = false;
      }
    }
    
    //saving gametype
    if(save)
    {
      //We take our well named variables and put them back into their original byte(s) in gametypeBytes
      gametypeBytes[6] = (byte) map;
      gametypeBytes[30] = (byte) imposterCount;
      gametypeBytes[1] = (byte) maxPlayers;
      gametypeBytes[40] = (byte) recommendedSettings;
      gametypeBytes[42] = (byte) confirmEjects;
      gametypeBytes[41] = (byte) emergencyCooldown;
      gametypeBytes[31] = (byte) killDistance;
      gametypeBytes[43] = (byte) visualTasks;
      gametypeBytes[23] = (byte) commonTasks;
      gametypeBytes[24] = (byte) longTasks;
      gametypeBytes[25] = (byte) shortTasks;
      
      byteBuffer.putInt(0, emergencyMeetings);
      gametypeBytes[26] = byteBuffer.get(0);
      gametypeBytes[27] = byteBuffer.get(1);
      gametypeBytes[28] = byteBuffer.get(2);
      gametypeBytes[29] = byteBuffer.get(3);
      
      byteBuffer.putInt(0, discussionTime);
      gametypeBytes[32] = byteBuffer.get(0);
      gametypeBytes[33] = byteBuffer.get(1);
      gametypeBytes[34] = byteBuffer.get(2);
      gametypeBytes[35] = byteBuffer.get(3);
      
      byteBuffer.putInt(0, votingTime);
      gametypeBytes[36] = byteBuffer.get(0);
      gametypeBytes[37] = byteBuffer.get(1);
      gametypeBytes[38] = byteBuffer.get(2);
      gametypeBytes[39] = byteBuffer.get(3);
      
      byteBuffer.putFloat(0, playerSpeed);
      gametypeBytes[7] = byteBuffer.get(0);
      gametypeBytes[8] = byteBuffer.get(1);
      gametypeBytes[9] = byteBuffer.get(2);
      gametypeBytes[10] = byteBuffer.get(3);
      
      byteBuffer.putFloat(0, crewmateVision);
      gametypeBytes[11] = byteBuffer.get(0);
      gametypeBytes[12] = byteBuffer.get(1);
      gametypeBytes[13] = byteBuffer.get(2);
      gametypeBytes[14] = byteBuffer.get(3);
      
      byteBuffer.putFloat(0, imposterVision);
      gametypeBytes[15] = byteBuffer.get(0);
      gametypeBytes[16] = byteBuffer.get(1);
      gametypeBytes[17] = byteBuffer.get(2);
      gametypeBytes[18] = byteBuffer.get(3);
      
      byteBuffer.putFloat(0, killCooldown);
      gametypeBytes[19] = byteBuffer.get(0);
      gametypeBytes[20] = byteBuffer.get(1);
      gametypeBytes[21] = byteBuffer.get(2);
      gametypeBytes[22] = byteBuffer.get(3);
      
      //We save the gametype we loaded up into the new gametype folder.
      try
      {
        FileOutputStream fileStream = new FileOutputStream(gameHostOptions);
        for(int i = 0; i < gametypeBytes.length; i++)
          fileStream.write(gametypeBytes[i]);
      }catch(IOException e)
      {
        e.printStackTrace();
      }
      
      //let the user know we saved everything :)
      System.out.println("Changes to " + gametypeName + " have been saved successfully~!!\n");
    }
  }
  
  //String gametypeFolderPath should be the folder of the gametype to be swapped in
  //String outputFile is the path of the file we should overwrite to swap in the gametype
  //This method loads the gameHostOptions into memory and then saves that back to the actual file.
  //This is because I copy pasted this code from a different project I wrote where I needed to change the file before I saved it.
  //I could have just moved the file, but I'm lazy and this way I can make sure the first byte is 3 :P
  private static void swapGametype(String gametypeFolderPath, String outputFile)
  {
    //File object for the gametype
    File gametypeFile = new File(gametypeFolderPath + File.separator + "gameHostOptions");
    
    //We create an int array to load the gametype into
    int[] gametype = new int[44];
    
    //we load in the raw byte values of the gametype file. 
    try
    {
      int next = -2;
      FileInputStream fileStream = new FileInputStream(gametypeFile);
      for(int i = 0; i < gametype.length; i++)
      {
        next = fileStream.read();
        if(next >= 0)
          gametype[i] = next;
      }
      fileStream.close();
    }
    catch(IOException e)
    {
      e.printStackTrace();
    }
    
    //Now we create an actual byte array to store the gametype in. We only made an int array because FileInputStream will only return ints.
    byte[] gametypeBytes = new byte[44];
    
    //We fill in each byte in the array with the byte value of the corresponding int from the int array
    for(int i = 0; i < gametypeBytes.length; i++)
      gametypeBytes[i] = Integer.valueOf(gametype[i]).byteValue();
    
    //We check to make sure that the first byte of the file is 3. If it isn't, we correct it and procced as normal.
    if(gametypeBytes[0] != 3)
    {
      System.err.println("Error! The first byte in this gametype did not start with 3! We have fixed this, but the gametype may be invalid or corrupt.");
    }
    
    //We save the gametype we loaded up into the save data folder
    try
    {
      FileOutputStream fileStream = new FileOutputStream(outputFile);
      for(int i = 0; i < gametypeBytes.length; i++)
        fileStream.write(gametypeBytes[i]);
    }catch(IOException e)
    {
      e.printStackTrace();
    }
  }
}