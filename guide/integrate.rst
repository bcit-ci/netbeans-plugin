###############
Integrated Help
###############

Integrated help ties you in to the online CodeIgniter User Guide.

*************
How To Use It
*************

There are several ways to use the integrated help:

1) Use the CodeIgniter search bar in the CodeIgniter toolbar.
    - This will open up your default browser and show you the CodeIgniter user guide search results 
2) Use the popup menu
    a) Right click on a method in a PHP file
    b) Select Go to CI Docs
    c) If the method is not a CodeIgniter method, an error tone will play. Otherwise:
        - If the method name is not ambiguous (i.e. only one class has a method with that name), the default browser will show the documentation for the selected method
        - Otherwise, you will be shown the CodeIgniter User guide search results for that method
3) Use the shortcut Ctrl+Alt+D when the carat is on the method you wish to see the documentation for. 
    This has the same behaviour as (2)

************
How It Works
************

The search bar uses the entered text to create a URL to the CodeIgniter user guide search page.

The popup menu first gets the name of the method selected. It compares it with a list of known CodeIgniter methods.
If no match is found, an error tone will play. Otherwise, it will see if the method name is ambiguous. In this case,
a URL for the CodeIgniter user guide search page is created. If the method name is not ambiguous, a URL is created to
navigate to the method documentation.

