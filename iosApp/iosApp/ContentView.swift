import SwiftUI

struct ContentView: View {
    var body: some View {
        VStack(spacing: 24) {
            Image(systemName: "rectangle.portrait.on.rectangle.portrait")
                .imageScale(.large)
                .font(.system(size: 64))
                .foregroundColor(.accentColor)
            Text("Memolki")
                .font(.largeTitle).bold()
            Text("iOS — coming soon")
                .font(.headline)
                .foregroundColor(.secondary)
        }
        .padding()
    }
}

#Preview {
    ContentView()
}
