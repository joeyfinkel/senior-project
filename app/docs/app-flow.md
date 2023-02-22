# The Application Flow

> ## Entrypoint
> The main file for the app
> is [MainActivity.kt](../src/main/java/com/example/myapplication/activities/MainActivity.kt).
> In `Main()`, you will find all of the app's
> routing logic. **To add new routes (or pages)**, add a new value to [Screens](#screens) and call
> the `composable` function with the screen's route. Here's an example function
> call: `composable(route) { PageContent() } `
>___
> [route](#route): `String` - The new page's route (`String`)<br/>
> `PageContent(): @Composable () -> Unit` - The content to render on the page
___

## Directories

- `activities`: Holds the app's activities
    - [MainActivity.kt](#entrypoint): The main file of the app as of now
- `components`: Holds all the UI elements
    - Subdirectories are there for code organization
    - Important files to be aware of:
        - [Layout.kt](../src/main/java/com/example/myapplication/components/Layout.kt): The app's
          main layout once the user is passed the login/registration
        - [DefaultButton.kt](../src/main/java/com/example/myapplication/components/DefaultButton.kt):
          Renders an orange button
        - [TextInput.kt](../src/main/java/com/example/myapplication/components/TextInput.kt):
          Renders `TextField` with default properties to keep it consistent throughout the app
- `state`: Holds the app's global state
    - [UserState.kt](../src/main/java/com/example/myapplication/state/UserState.kt): An `Object`
      that holds user information that can be used throughout the entire app
        - **Allows us to minimize the total amount of `SQL queries`**
- `ui.theme`: Holds the app's default theme
    - [Color.kt](../src/main/java/com/example/myapplication/ui/theme/Color.kt): The app's default
      colors
    - [Constants.kt](../src/main/java/com/example/myapplication/ui/theme/Constants.kt): Constant
      values for styling UI elements that are used in multiple places throughout the app
    - [Theme.kt](../src/main/java/com/example/myapplication/ui/theme/Theme.kt): The app's theme
    - [Type.kt](../src/main/java/com/example/myapplication/ui/theme/Type.kt): The app's typography

## Screens

> An `enum class` that has a value for every screen of the application.
>
> ### Parameters
> #### route
> - The route of the screen
> #### title
> - **_Optional_**
> - The title of the screen