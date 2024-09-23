//
//  TripleTemplate.h
//  TripleTemplate
//
//  Created by Christopher Lawton on 9/12/24.
//

#ifndef TripleTemplate_h
#define TripleTemplate_h

template <typename T>
struct Triple{
    T a;
    T b;
    T c;
    
    T add();
};

template <typename T>
T Triple<T>::add(){
    return a + b + c;
};

#endif /* TripleTemplate_h */
