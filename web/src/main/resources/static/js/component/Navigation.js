/**
 * Created by woniper on 15. 8. 27..
 */
var Navigation = React.createClass({
   render: function() {
       return (
           <div id="wrapper">
           <nav className="navbar navbar-inverse navbar-fixed-top" role="navigation">
               <div className="navbar-header">
                   <button type="button" className="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                       <span className="sr-only">Toggle navigation</span>
                       <span className="icon-bar"></span>
                       <span className="icon-bar"></span>
                       <span className="icon-bar"></span>
                   </button>
                   <a className="navbar-brand" href="index.html"> Woniper </a>
               </div>

               <ul className="nav navbar-right top-nav">
                   <li>
                       <a href="login.html">
                           <i className="fa fa-user"></i> Login
                       </a>
                   </li>
               </ul>

               <div clasNames="collapse navbar-collapse navbar-ex1-collapse">
                   <ul className="nav navbar-nav side-nav">
                       <li>
                           <a href="index.html">
                               <i className="fa fa-fw fa-dashboard"></i> Dashboard
                           </a>
                       </li>
                       <li class="active">
                           <a href="boards.html">
                               <i className="fa fa-fw fa-table"></i> 게시판
                           </a>
                       </li>
                   </ul>
               </div>
           </nav>
           </div>
       )
   }
});

React.render(<Navigation />, document.getElementById("navigation"));