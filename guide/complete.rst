###############
Code Completion
###############

Code Completion provides method suggestions as you work on your code.

*************
How To Use It
*************

When you enter the magic arrow (-&gt;) after a variable name, suggested methods
popup in a selectable list, if the variable's object type can be
determined.

If you have already type a quote for string, then press ctrl+space, then
suggested view file path will also popup in a selectable list.

************
How It Works
************

The way how most of the code completion work is the same as
http://stackoverflow.com/questions/9308604/how-to-integrate-codeigniter-with-netbeans-fully
where our plugin will automatically generate that file for it if you are 
currently in a CodeIgniter project.

For the view completion, the selectable list will be all the .php files inside
the View folder (and its subfolders). 
