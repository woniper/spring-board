/**
 * Created by woniper on 15. 8. 27..
 */
var Navigation = React.createClass({
    getInitialState: function() {
        return {
            isSession: false,
            username: null,
            name: null
        }
    },
   componentDidMount: function() {
       var state = this;
       $(document).ready(function() {
           $.ajax({
               type: "GET",
               url: "/web-session",
               success: function(data) {
                   state.setState({
                       isSession: data.session,
                       username: data.username,
                       name: data.name
                   });

               }
           })
       })
   },
   render: function() {
       var loginItem = [];
       if(this.state.isSession) {
           loginItem.push(
               <ul className="nav navbar-right top-nav">
                   <li className="dropdown">
                       <a href="#" className="dropdown-toggle" data-toggle="dropdown">
                           <i className="fa fa-user"></i> {this.state.name} <b className="caret"></b>
                       </a>
                       <ul className="dropdown-menu">
                           <li>
                               <a href="#"><i className="fa fa-fw fa-user"></i> Profile</a>
                           </li>
                           <li>
                               <a href="#"><i className="fa fa-fw fa-gear"></i> Settings</a>
                           </li>
                           <li className="divider"></li>
                           <li>
                               <a href="/logout"><i className="fa fa-fw fa-power-off"></i> Log Out</a>
                           </li>
                       </ul>
                   </li>
               </ul>
           )
       } else {
           loginItem.push(
               <ul className="nav navbar-right top-nav">
                   <li>
                       <a href="/login">
                           <i className="fa fa-user"></i> Login
                       </a>
                   </li>
               </ul>
           )
       }
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
                   <a className="navbar-brand" href="/"> Woniper </a>
               </div>

               {loginItem}

               <div clasNames="collapse navbar-collapse navbar-ex1-collapse">
                   <ul className="nav navbar-nav side-nav">
                       <li>
                           <a href="/dashboard">
                               <i className="fa fa-fw fa-dashboard"></i> Dashboard
                           </a>
                       </li>
                       <li class="active">
                           <a href="/">
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