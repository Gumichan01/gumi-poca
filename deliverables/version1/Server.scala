//
//  Server
//  evaluator
//
//  Created by Yves VIAUD on 11/11/2017.
//

// I do not know yet how to really implement it because it will be coupled with the web framework
// Could we use a singleton (using `object`) but then it is not really a class anymore

class Server {
  val users: List[User]
  val courses: List[Course] // how will we load courses...
}
