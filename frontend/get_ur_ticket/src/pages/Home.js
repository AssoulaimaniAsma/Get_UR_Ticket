import React, { useState, useEffect } from 'react';
import EventCard from '../components/EventCard';
import { getAllEvents, getAllCategories } from '../services/api';

function Home() {
    const [events, setEvents] = useState([]);
    const [categories, setCategories] = useState([]);
    const [selectedCategory, setSelectedCategory] = useState('all');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [eventsData, categoriesData] = await Promise.all([
                getAllEvents(),
                getAllCategories()
            ]);
            setEvents(eventsData);
            setCategories(categoriesData);
            setLoading(false);
        } catch (error) {
            console.error('Erreur lors du chargement des données:', error);
            setLoading(false);
        }
    };

    const filteredEvents = selectedCategory === 'all'
        ? events
        : events.filter(event => event.category?.id === parseInt(selectedCategory));

    if (loading) {
        return <div className="loading">Chargement des événements...</div>;
    }

    return (
        <div className="container">
            <div className="page-header">
                <h1>Découvrez nos événements</h1>
                <p>Trouvez et réservez vos événements préférés</p>
            </div>

            <div className="filter-section">
                <label>
                    <strong>Filtrer par catégorie:</strong>
                    <select
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                        style={{marginLeft: '1rem'}}
                    >
                        <option value="all">Toutes les catégories</option>
                        {categories.map(category => (
                            <option key={category.id} value={category.id}>
                                {category.nom}
                            </option>
                        ))}
                    </select>
                </label>
            </div>

            <div className="events-grid">
                {filteredEvents.length > 0 ? (
                    filteredEvents.map(event => (
                        <EventCard key={event.id} event={event} />
                    ))
                ) : (
                    <p>Aucun événement disponible</p>
                )}
            </div>
        </div>
    );
}

export default Home;