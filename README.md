# Doctor
A lightweight Java dependency injection framework ([JSR-330](http://javax-inject.github.io/javax-inject/))

# Adding Doctor to your project
```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Then:

```kotlin
dependencies {
    implementation("rip.hippo:Doctor:1.0.1")
}
```

# Usage

```java
// Create a module
private static final DoctorModule MY_MODULE = new AbstractModule() {
  @Override
  protected void configure() {
      bind(MyService.class).to(MyServiceImpl.class);
  }
};

public static void main(String[]args){
    // Create an injector instance
    Injector injector = Doctor.createInjector(MY_MODULE);
    // Get an instance of MyService
    MyService myService = injector.getInstance(MyService.class);
}
```