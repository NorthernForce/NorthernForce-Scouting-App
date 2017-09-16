# NorthernForce-Scouting-App
Android App to be used for scouting.

Database Schema:

config file located in assets folder

Teams
int Team_Number
String Defining_Characteristics
int Average_Score
String High_School

Matches
int Team_Number
int Match_Number
String Event_Name
int Allie_One (team number)
int Allie_Two (team number)
int Opponent_One (team number)
int Opponent_Two (team number)
int Opponent_Three (team number)

Performance
int Team_Number
int Match_Number
int Score
int Performance


Events
String Event_Name
String Date
String Location

Class Structure:
MainActivity give the user buttons to enter data, view data, or use bluetooth to sync
Enter data button -> EnterDataActivity
View data button -> ViewDataActivity
Bluetooth button -> BluetoothActivity

EnterDataActivity lets the user enter data
The class generates a linear layout with elements correspendoing the dataEnteryViews array in UIDatatbaseInterface the elements of the array is DataEnteryRow
When the submit button is rpessed it takes the current view and passes it to UIDatabaseInterace.submitDataWEntery()

ViewDatatActiivty lets the user view the data by database table
It gets the list of tables from UIDatatbaseInterface.getTableNames() and put them in a spinner for the user to select
It creates a grid view to view the data using the ViewDataAdapter
When an item from the spinner is selected UIDataBaseInterface.setCUrrentDataViewTable is called with the name of the selected table and then the grid view is recreated.

ViewDataAdapter takes the data in the database and puts it into the grid view for the user to see
It gets the table to display from UIDatabaseInterace.getCurrentDataViewTable()
The method getView takes the position of currentView as well as parent and gets a cursor from the database based on a select from table. and for the given position, it puts all the neccessary data in a textView to return and then be put in the gridView

UIDatabaseInterface acts as a interface between the UI and the database making it possible to transfer data back and forth