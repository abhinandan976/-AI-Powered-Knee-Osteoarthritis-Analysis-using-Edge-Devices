# AI-Powered Knee Osteoarthritis Analysis using Edge Devices

## 📱 Project Overview

This project is an **AI-powered Android application** for the **detection and grading of Knee Osteoarthritis (KOA)** from X-ray images using deep learning. It was developed as part of our **6th sem academic project** at **KLE Technological University, Belagavi**.

The app is designed to perform real-time, fully offline KOA detection and severity grading, specifically tailored for rural and resource-constrained medical settings where access to radiologists is limited.

---

## 🎯 Key Features
- Real-time KOA detection on Android devices (inference < 1 second)
- KOA severity grading from **KL Grade 0 to 4**
- Offline-first, runs without internet
- Firebase-backed secure Electronic Information System (EIS) for storing patient records
- Multi-role access for doctors, radiologists, and assistants

---

## 🧠 Tech Stack

### **AI / ML**
- PyTorch
- PyTorch Mobile
- MobileNetV3 (Compact CNN)
- Grad-CAM
- Transfer Learning
- Model Optimization (Pruning & Quantization)

### **Android App Development**
- Android Studio
- Java / Kotlin
- XML

### **Backend**
- Firebase Realtime Database
- Firebase Cloud Storage

---

## 📊 Dataset
- 6,500 labeled knee X-ray images for training
- Optimized model size: **16.66MB → 16.65MB** through pruning and quantization
- Final accuracy achieved: **77.60%**

---

## 🚀 How It Works
1. User uploads or captures knee X-ray.
2. AI model runs locally using **PyTorch Mobile**.
3. KOA severity (KL Grade 0-4) is predicted.
4. Grad-CAM heatmaps visually explain the prediction.
5. Data optionally stored in Firebase backend.

---

## 🏥 Impact
This solution is designed to aid **healthcare workers in rural areas** by providing fast, accessible, and explainable diagnostics without requiring cloud computing or radiologist expertise on-site.

---

## 📂 Folder Structure
Torchapp/
├── app/
├── model/
│ ├── mobilenetv3.pth
├── firebase/
├── README.md
└── ...



---

## 💡 Future Work
- Expanding dataset with more diverse X-ray sources
- Improve model accuracy with newer lightweight deeplearning model architectures
- Integrate with additional health diagnostic services

---

## 🏷️ Skills Highlighted
- Android Development (Android SDK, Java/Kotlin)
- Deep Learning
- Model Optimization
- Explainable AI (XAI)
- Edge AI Deployment
- Firebase Integration

---

## 🤝 Acknowledgments
Thanks to **KLE Technological University, Belagavi** for support and guidance during this project.

---

## 📄 License
This project is for academic use only.

---

## 🔗 Connect
GitHub: [abhinandan976](https://github.com/abhinandan976)
