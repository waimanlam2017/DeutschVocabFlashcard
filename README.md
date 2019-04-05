# DeutschVocabFlashcard

Hello, this software was created to help remembering German article der/die/das with the help of a little Flashcard Game. Please use this software as your wish.


Description
---
This software use a SQLite database (Vokabeln.sqlite) preloaded with A1 German noun with der/die/das and display the flashcard game randomly.
By default it load 10 words in each invocation/load.

It included a built-in review interval logic, it would pick 10 words which has a revise date matching system's date.
For example, here is a word:

Article - 'der'

Word - 'Abend'

Last_Review_Date - 2019-02-01

Next_Review_Date - 2019-02-26

Next_Review_Inteval - 1

The software would check the column 'Next_Review_Date' to see whether to revise this word. After a successful game, the database would be updated:

For example:

Article - 'der'

Word - 'Abend'

Last_Review_Date - 2019-04-05

Next_Review_Date - 2019-04-06

Next_Review_Inteval - 2

The column 'Next_Review_Date' is updated to the value of 'Last_Review_Date' plus 1, and the column 'Next_Review_Inteval' is updated to 2.

The logic of updating column 'Next_Review_Inteval' increase by its power of 2 : 1->2->4->8->16->32 so that you don't revise the same words every other day.



Requirement
---
Java 1.8 with JavaFX ( oracle jdk or openjdk with javafx )



Useful Software
---
SQLite Browser  ( https://sqlitebrowser.org/ )



Package description
---
/data - contain Vokabeln.sqlite

/pic - contain a startup picture 'gutentag.jpg', 'gutentag.jpg' is the hardcoded default

/src - Source
