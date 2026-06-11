# CaptureEasy

> Simple, fast, and reliable screenshot capture utility for test automation, bug reporting, and operational evidence collection.

## Overview

CaptureEasy is a lightweight application designed to simplify screenshot and evidence capture workflows. Whether you're executing automated tests, performing manual validation, documenting issues, or collecting operational evidence, CaptureEasy helps you capture, organize, and share screenshots efficiently.

---

## ✨ Features

### 📸 Instant Screenshot Capture
- Capture the entire screen
- Capture a selected window
- Capture a custom region

### 📁 Organized Storage
- Automatic file naming
- Timestamp-based organization
- Custom save locations

### ⚡ Lightweight & Fast
- Minimal resource consumption
- Quick startup time
- Responsive user experience

### 🧪 QA & Testing Friendly
- Capture test execution evidence
- Support defect reporting workflows
- Simplify test documentation

### 🔒 Secure
- Local file storage
- No external data transmission
- Suitable for restricted or air-gapped environments

---

## 🎯 Use Cases

### Test Automation
Capture screenshots during:
- Selenium execution
- Playwright execution
- Mobile automation testing
- API test evidence generation

### Manual Testing
- Defect reporting
- UAT evidence collection
- Regression testing documentation

### Operations & Support
- Incident documentation
- Environment verification
- Audit evidence collection

---

## 🚀 Installation

### Prerequisites

- Java 17 or later
- Windows, Linux, or macOS

### Run the Application

```bash
java -jar CaptureEasy.jar
```

---

## ⚙️ Configuration

Example configuration:

```properties
capture.output.directory=./captures
capture.timestamp.format=yyyyMMdd_HHmmss
capture.image.format=png
```

---

## 📂 Directory Structure

```text
captures/
└── 2026-06-11/
    ├── screenshot_001.png
    ├── screenshot_002.png
    └── screenshot_003.png
```

---

## 🔄 Example Workflow

1. Launch CaptureEasy
2. Select a capture mode
3. Capture the desired screen or region
4. Review the captured image
5. Save or share the evidence

---

## ✅ Benefits

| Benefit | Description |
|----------|-------------|
| Faster Documentation | Reduce time spent collecting screenshots |
| Better Traceability | Automatically timestamp captures |
| Improved Reporting | Provide clear visual evidence |
| Easy Adoption | Simple and intuitive interface |

---

## 🛣️ Roadmap

Planned enhancements:

- Image annotation tools
- OCR support
- Video recording
- Automated capture scheduling
- Cloud storage integration
- Allure Report integration

---

## 🤝 Contributing

Contributions are welcome.

### Create a Feature Branch

```bash
git checkout -b feature/new-feature
```

### Commit Your Changes

```bash
git commit -m "Add new feature"
```

### Push Changes

```bash
git push origin feature/new-feature
```

### Open a Pull Request

---

## 📜 License

This project is licensed under the MIT License.

---

## CaptureEasy

**Capture. Document. Share. Simplify.**

A smarter way to collect screenshots and evidence for testing, support, and operations.
