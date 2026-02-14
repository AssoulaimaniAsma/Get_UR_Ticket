import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import EventCard from '../../components/EventCard';
import { getAllEvents, getAllCategories } from '../../services/api';

function Home() {
    const navigate = useNavigate();
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
        return <div className="loading">⏳ Chargement des événements...</div>;
    }

    return (
        <div className="container">
            <div className="page-header">
                <div>
                    <h1>🎉 Découvrez nos événements</h1>
                    <p>Trouvez et réservez vos événements préférés</p>
                </div>
            </div>

            <div className="filter-section">
                <label>
                    <strong>📂 Filtrer par catégorie:</strong>
                    <select
                        value={selectedCategory}
                        onChange={(e) => setSelectedCategory(e.target.value)}
                        style={{marginLeft: '1rem'}}
                    >
                        <option value="all">🌟 Toutes les catégories</option>
                        {categories.map(category => (
                            <option key={category.id} value={category.id}>
                                {category.iconUrl} {category.nom}
                            </option>
                        ))}
                    </select>
                </label>
            </div>

            {filteredEvents.length > 0 ? (
                <div className="events-grid">
                    {filteredEvents.map(event => (
                        <EventCard 
                            key={event.id} 
                            event={event}
                            onClick={() => navigate(`/event/${event.id}`)}
                        />
                    ))}
                </div>
            ) : (
                <div className="empty-state">
                    <div className="empty-state-icon">📭</div>
                    <h3>Aucun événement disponible</h3>
                    <p>Aucun événement ne correspond à vos critères</p>
                </div>
            )}
        </div>
    );
}

export default Home;