# Attendance-Tracker
A simple Attendance Tracker app (for now) which will refine my UI/UX skills along with Kotlin.

The app will be a blend of ui.theme (Jetpack Compose) components and layout xml. Java maybe used throughout the app.

Also, since I admire AnkiDroid (also a part-time contributor there) and am fond of their AI Policy, I will be not using any form of LLMs to code generically throughout the entire app (and if anyone wishes to contribute they must strictly follow this too). However, AI/LLMs may be used for research/debugging and whenever used will be explicitly mentioned.

External Libraries/Dependencies used:

Room-Used for all the Database storing done inside a SQLite DB, learning experience for me since I usually work with NoSQL based databases. The database at this point contains two tables, subjects and attendance. subjects has two columns id and name, attendance has 5 columns: id, subjectId (foreignkey relation with subject), date, isPresent and lectureCount. For now, I'm not storing the attendance inside a column and calculating it everytime. If that comes up as a better option I'll add a new column for attendancePercentage. Will update the README properly for db structure later.
