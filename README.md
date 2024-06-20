<div align="center">
  <h1> Mobile Development </h1>
  <img src="https://akcdn.detik.net.id/visual/2019/08/23/e412ce12-121b-4c45-a498-4cc683075f4d_169.jpeg?w=400&q=90" alt="Android" width="500">
  <h3>Your FMD Detection Partner</h2>
</div>
<br>
<h2>OVERVIEW</h2>
<p1>SnapMoo is an application that can help farmers, especially cattle, to detect Foot and Mouth Disease (FMD), and provide appropriate treatment advice. This application contains information about FMD, a scan feature for FMD calcification, and also suggestions for proper and fast treatment via mobile phone.</p1>
 
<div align="center">
  <h3>Logo</h3>

|   Outline   |    Rounded     |
|-------------|-----------------------|
|   <img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/icon_stroke.png" alt="Logo" width="200">|   <img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/icon_logo.png" alt="Logo" width="200"> | 
</div>
<br>

<h2>Tools</h2>

|   Figma     |  Android Studio |      
|-------------|-----------------|
| <img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/icon_figma.png" alt="android-studio" width="100"> | <img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/icon_andorid-studio.png" alt="android-studio" width="100">|

<h2>User Flow</h2>
<img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/User%20Experience.png" alt="resources" width="600"> 

<h2>Resources</h2>
<img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/resource.png" alt="resources" width="600"> 
 
<h2>Wireframe</h2>
<img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/Wirefreame.png" alt="wireframe" width="600"> 

<h2>UI/UX Design</h2>
<img src="https://github.com/snapmoo/snapmoo/blob/main/assets/Mobile%20Development/UIUX.png" alt="uiuxdesign" width="600"> 



<h2>APLLICATION DEVELOPMENT</h2>
<details>
<summary>
<h3>Flow Project</h3>
</summary>
<ol>
  <li>UserFlow:  The first step is to identify how users interact with the SnapMoo application. This User Flow provides an overview of the user flow on each page.</li>
  <li>WorkFlow: Then, define the application workflow that includes the process of data flow and interaction of various system components and ensure all functions run optimally.</li>
  <li>WireFrame: Wireframes are created to design the rough skeleton of the user interface. Helps organize the layout and navigation of the application. </li>
  <li>UI/UX: Develops a detailed and interactive user interface and user experience design according to the previous wireframes. This involves selecting color palettes, fonts, icons and visual elements that ensure optimal user experience.</li>
  <li>User Authentication: Implement a secure user authentication system such as register process for new users, login, and user sessions. This feature ensures that only registered users can access application features.</li>
  <li>News Information Retrieval: Create a news feature that is taken from trusted sources that enter the cloud database of the application. The information displayed can be an education for breeders in FMD cases.</li>
  <li>FMD Classifier Functioning: Implementing FMD Classification in cattle from Machine Learning model. From this feature the user takes a photo of the cow and gets an analysis result whether it is affected by FMD or not and gets advice for handling </li>
  <li>Report for Request Handling: Implement the report feature if the cow is positive for FMD to request assistance to the FMD Handling Task Force, the report is sent to the email to the person concerned.</li>
  <li>Real-Time Functioning (Demo): Demo the application to ensure all implemented features can run in real time. This demo will help recognize problems in the application if there are bugs.</li>
  <li>Finishing App:  The last step when all application features are running optimally, and ensure all elements have been implemented correctly.</li>
</ol>
</details>
<details>
<summary>
<h3>Library</h3>
</summary>
  <ol>
  <li>Retrofit: Facilitates communication with APIs such as retrieving data from the server and displaying it in the application.</li>
  <li>
  OkHttp: Backend for Retrofit that ensures HTTP requests, and adds features such as request/response logging.
  </li>
  <li>
 CameraX: For the Camera function that allows taking pictures from the device to scan the cattle.
Hdodenhof's CircleImageView: to make the image display round on the Application
  </li>
  <li>
Lottie: Adding animations that make the interface interesting, such as loadiang animations, and on the splashscreen.
  </li>
  <li>
    uCrop Yalantis: Allows the user to crop the image to the desired size before submitting it for analysis. 
  </li>
  <li>
    TensorFlow-Lite: runs a machine learning model on the application to analyze cattle images and classify whether they are affected by FMD or not.
  </li>
</ol>
</details>

<br>
<h2>INSTALATION</h2>
<p1>The SnapMoo application that we developed is an Android Operating System-based application with a minimum android version, namely Android Oreo. SnapMoo application can be used on android devices with android version Oreo, Pie, Quience Tart, Red Velvet Cake, Snow Cone, Tiramisu, Upside Down Cake, Vanilla Ice Cream or the latest version. Can be downloaded on the button below </p1>
<br>
<h4>GetAPP</h4>

[![Download SnapMoo](https://img.shields.io/badge/Download%20SnapMoo-blue?style=for-the-badge)](https://github.com/snapmoo/SnapMoo-MD/releases/download/v1.0.0/app-debug.apk)




