import SwiftUI
import Shared // This imports your KMP commonMain/iosMain framework

struct ContentView: View {
    var body: some View {
        // This embeds your Compose Multiplatform UI directly inside SwiftUI
        ComposeView()
            .ignoresSafeArea(.all, edges: .bottom) // Optional: lets Compose handle the screen edges
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        // Calls the Kotlin function we checked in Step 1
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}
