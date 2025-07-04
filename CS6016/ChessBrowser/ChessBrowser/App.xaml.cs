namespace ChessBrowser;

public partial class App : Application
{
	public App()
	{
		TestParser.run();
		// Console.Write("WUT");
		InitializeComponent();
    MainPage = new NavigationPage( new MainPage() );
	}
}
