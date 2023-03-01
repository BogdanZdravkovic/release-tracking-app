import React, { Component } from 'react';
import './App.css';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ReleaseList from './ReleaseList';
import ReleaseEdit from "./ReleaseEdit";

class App extends Component {
  render() {
    return (
        <Router>
          <Switch>
            <Route path='/' exact={true} component={ReleaseList}/>
            <Route path='/releases' exact={true} component={ReleaseList}/>
            <Route path='/releases/:id' component={ReleaseEdit}/>
          </Switch>
        </Router>
    )
  }
}

export default App;