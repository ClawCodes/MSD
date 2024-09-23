//
//  main.cpp
//  sfml_homebrewinstallation
//
//  Created by Christopher Lawton on 9/16/24.
//

#include <iostream>
#include <SFML/Graphics.hpp>



int main(int argc, const char * argv[]) {
    
    // They want the game interface to be:
//    Game game;
//    game.run();

    // Get canvas
    sf::RenderWindow window (sf::VideoMode(800, 600), "My first SFML game");
    
    sf::RectangleShape rectangle;
    
    // Left top corner is 0,0
    // anything positive x moves right and positive y moves down
    rectangle.setPosition(100, 200);
    
    rectangle.setSize(sf::Vector2<float>(200, 100));
    rectangle.setFillColor(sf::Color::Red);
    
    // Keep the window live
    while(window.isOpen()){
        sf::Event event;
        
        while(window.pollEvent(event)){
            if (event.type == sf::Event::Closed)
                window.close();
        }
        window.clear();
        window.draw(rectangle);
        window.display();
        
        // General flow
        // processEvent()
        // update()
        // Render()
        
        
        
    }
    
    
    
    return 0;
}
