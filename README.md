# 📱 Mentor Selector – Mobile Application (Kotlin)

Mentor Selector is an Android mobile application developed using Kotlin to simplify the process of assigning mentors to students based on their academic performance (CGPA). The application helps reduce manual effort and improves efficiency in mentor allocation within educational institutions.

---

## 🎯 Objective

The main objective of this project is to automate the process of assigning mentors to students based on their CGPA. This reduces the time and effort required by faculty members to manually sort and assign mentors.

---

## 📖 Project Description

The application provides two types of user roles:

### 👨‍💼 Admin (HOD)

* Manages staff details
* Assigns staff advisors for each class
* Controls mentor allocation process

### 👨‍🏫 Staff Advisor

* Accesses student details
* Selects mentor preferences (e.g., gender)
* Generates mentor assignments automatically

---

## ⚙️ Features

* 🔐 Role-based login system (Admin & Staff Advisor)
* 📋 Student data management (Name, Department, Section, Year, CGPA)
* 🎲 Random mentor selection logic
* ⚡ Automated mentor assignment based on CGPA
* ☁️ Firebase integration for data storage

---

## 🛠️ Tech Stack

* **Language:** Kotlin
* **Platform:** Android
* **Backend/Database:** Firebase
* **IDE:** Android Studio

---

## 🔄 Workflow

1. Admin logs in and assigns staff advisors
2. Staff advisor logs in and views student details
3. Selects mentor criteria (e.g., gender)
4. System randomly assigns mentors based on CGPA
5. Mentor allocation is generated instantly

---

## 📂 Project Structure

```
Mentor-Selector-Mobile-Application/
│── app/
│   ├── java/
│   │   ├── activities/
│   │   ├── models/
│   │   ├── adapters/
│   ├── res/
│   │   ├── layout/
│   │   ├── drawable/
│── Firebase/
│── AndroidManifest.xml
```

---

## 📸 Screenshots

Staff Advisor Code:
<img width="1366" height="768" alt="Staff Advisor Code" src="https://github.com/user-attachments/assets/9e3dd97b-1348-421b-81dd-82bcf227248a" />

Random Picker Code:
<img width="1366" height="768" alt="Random Picker Code" src="https://github.com/user-attachments/assets/11501508-4d88-4714-b1fc-c74a4f0af039" />

 
Firebase contents:
 <img width="1366" height="768" alt="Firebase contents1" src="https://github.com/user-attachments/assets/b324e693-4d36-476d-9e54-27905d2c2a3b" />
<img width="1366" height="768" alt="Firebase contents2" src="https://github.com/user-attachments/assets/b07a4ee9-aa7c-4130-a18b-3245d5beae06" />
<img width="1366" height="768" alt="Firebase contents3" src="https://github.com/user-attachments/assets/cf0a2940-2d24-4aaa-aa47-0b56cbba61a1" />

Activity_XML:
<img width="1366" height="768" alt="Activity_XML1" src="https://github.com/user-attachments/assets/16d740b5-40a3-4e24-81b3-58a524b226b8" />
<img width="1366" height="768" alt="Activity_XML2" src="https://github.com/user-attachments/assets/a178ee72-6252-4e02-9fc9-2fca445a8f95" />
<img width="1366" height="768" alt="Activity_XML3" src="https://github.com/user-attachments/assets/2007bb19-bbda-43fd-8896-745a48547e82" />
<img width="1366" height="768" alt="Activity_XML4" src="https://github.com/user-attachments/assets/8500cd98-37a1-4850-b129-1335e42808b1" />
<img width="1366" height="768" alt="Activity_XML5" src="https://github.com/user-attachments/assets/efb0fedb-3da1-48c7-a902-37ef9ab49b7e" />
<img width="1366" height="768" alt="Activity_XML6" src="https://github.com/user-attachments/assets/cff92fdd-3e7f-4f1d-8131-1fd7271b03ff" />
<img width="1366" height="768" alt="Activity_XML7" src="https://github.com/user-attachments/assets/38b15a80-3889-4927-880b-e3b8a4f7741d" />
<img width="1366" height="768" alt="Activity_XML8" src="https://github.com/user-attachments/assets/b5f15e28-622b-49bf-8313-1029fca74070" />
<img width="1366" height="768" alt="Activity_XML9" src="https://github.com/user-attachments/assets/7d957790-e9f6-4035-aef5-25b82a97d2b4" />

---

## ▶️ How to Run

1. Clone the repository:

```
git clone https://github.com/animan31802-design/Mentory-Selector-Mobile-Application.git
```

2. Open in Android Studio

3. Sync Gradle and install dependencies

4. Run the application on emulator or device

---

## 🔮 Future Enhancements

* 🔐 Secure authentication system
* 📊 Smart mentor matching (AI-based instead of random)
* 💬 Communication between mentor and student
* 📅 Scheduling and session management
* ⭐ Rating and feedback system

---

## ✅ Conclusion

The Mentor Selector application provides an efficient solution for assigning mentors to students. It minimizes manual work and ensures a quicker and structured mentor allocation process within educational institutions.

---

## 👨‍💻 Authors

* **Manojkumar M**

---

## ⭐ Support

If you found this project useful, consider giving it a ⭐ on GitHub!
