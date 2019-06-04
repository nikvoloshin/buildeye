import React from 'react';
import {Redirect, Route, Switch} from "react-router";
import BuildsList from "../builds-list/BuildsList";
import BuildPage from "../build-page/BuildPage";
import Header from "../header/Header";
import styles from './app.module.css'

const App: React.FC = () => (
    <>
        <Header className={styles.header}/>
        <div className={styles.content}>
            <Switch>
                <Route exact path="/" component={BuildsList}/>
                <Route path="/:id" component={BuildPage}/>
                <Route path="*"><Redirect to="/" /></Route>
            </Switch>
        </div>
    </>
);

export default App;
