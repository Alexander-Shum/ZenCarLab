# ZenCarLab

ZenCarLab - это небольшое Android-приложение, разработанное с использованием современных технологий и подходов. Проект создан с целью демонстрации моих навыков для компании ZEN.CAR

<p align="center">
  <img src="https://github.com/user-attachments/assets/18e680c2-d2c7-4b48-a0c5-dd0cd0e59626" alt="App Screenshot 1" width="300"/>
  <img src="https://github.com/user-attachments/assets/beb0de7c-956c-43e1-a155-d3d39790107b" alt="App Screenshot 2" width="300"/>
  <img src="https://github.com/user-attachments/assets/d7928252-547d-4f44-aba5-d43499802d33" alt="App Screenshot 3" width="300"/>
</p>
## Стек технологий

Проект реализован с использованием следующих технологий и библиотек:

- **Kotlin Coroutines**
- **Jetpack Compose**
- **MVVM (Model-View-ViewModel)**
- **Room**
- **Coil**
- **Navigation Compose с type safety**
- **Koin**

## Архитектура проекта

Проект состоит из трех модулей:

- **Data** - модуль, отвечающий за работу с базой данных и API.
- **Domain** - бизнес-логика и модели данных.
- **App/Presentation** - UI и отображение данных с использованием Jetpack Compose.

Такой подход был выбран исходя из небольшого масштаба проекта, с минимальной необходимостью масштабирования.
  
## Как запустить проект

1. Склонируйте репозиторий:
    ```bash
    git clone https://github.com/your-repo/ZenCarLab.git
    ```
2. Откройте проект в Android Studio.
3. Запустите сборку и выполните запуск на эмуляторе или реальном устройстве.

## Лицензия

Этот проект распространяется под лицензией MIT. Подробнее см. в [LICENSE](LICENSE).
