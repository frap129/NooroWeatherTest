# Nooro Weather Test
This is the result of my work on a take-home assignment from Nooro. The contents of this repository were completed in just under 5 hours.

## Getting Started
1. Clone this repository and open it in Android Studio (Ladybug or newer)
2. Open `local.properties` and add
    ```
    weatherApiKey="<your weatherapi key>"
    ```
3. Click the green "Run" arrow to deploy to a connected or virtual device (must be API 28 or newer)

## Potential improvements
Due to the time limitation, I was not able to complete everything. Imprevemnts that could be made given more time include
- Error handling
- Unit/Integration/UI tests
- Detect location using GNSS
- Load search results as you type, with throttling/debouncing
- Dark mode theme
- Add a circle progress indicator to the loading state
