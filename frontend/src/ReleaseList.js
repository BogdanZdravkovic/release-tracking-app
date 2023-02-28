import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

class ReleaseList extends Component {

    constructor(props) {
        super(props);
        this.state = {releases: []};
        this.remove = this.remove.bind(this);
    }

    componentDidMount() {
        fetch('/releases')
            .then(response => response.json())
            .then(data => this.setState({releases: data}));
    }

    async remove(id) {
        await fetch(`/releases/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updateReleases = [...this.state.releases].filter(i => i.id !== id);
            this.setState({releases: updateReleases});
        });
    }

    render() {
        const {releases, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const releaseList = releases.map(release => {
            return <tr key={release.id}>
                <td style={{whiteSpace: 'nowrap'}}>{release.name}</td>
                <td>{release.description}</td>
                <td>{release.status}</td>
                <td>{release.releaseDate}</td>
                <td>{release.createdAt}</td>
                <td>{release.lastUpdateAt}</td>
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/releases/" + release.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(release.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/releases/new">Add new release</Button>
                    </div>
                    <h3>Releases</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="10%">Name</th>
                            <th width="10%">Description</th>
                            <th width="10%">Status</th>
                            <th width="10%">Release date</th>
                            <th width="10%">Created</th>
                            <th width="10%">Last updated</th>
                            <th width="40%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {releaseList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }
}
export default ReleaseList;