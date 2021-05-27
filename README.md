Jerry Yu, JR2YU, 20764520 <br>
Ubuntu 18.04 LTS <br>
JDK 11 with Android API 30 <br> 

Refresh button was taken from: https://www.pngfind.com/mpng/xxohJh_circle-of-two-clockwise-arrows-rotation-comments-refresh/

Exit/Delete Button image was taken from: https://thenounproject.com/term/x-button/

There are 3 fragments. Addition fragment is where you add gestures to the library.
Draw a gesture, click add and give a name to the gesture and afterwards, a gesture is added
to a shareviewmodel.

The second fragment is the library fragment. It displays all the gestures along with their name.
We can delete from the library using the 'X' button or create a new gesture by clicking the 'refresh' button.

Finally, the recognize fragment is where you draw a one stroke gesture and the top
3 gestures appear on the bottom.

Saving is done onPause() and reloading on onResume(). I store 128 x and y points and the name
for each gesture and rebuild the gesture from its points when onResume() is triggered.



