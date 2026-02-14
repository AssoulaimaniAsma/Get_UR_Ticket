import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { createEvent, getAllCategories, getCurrentUser } from '../../services/api';

function CreateEvent() {
    const navigate = useNavigate();
    const user = getCurrentUser();
    const [categories, setCategories] = useState([]);
    const [message, setMessage] = useState('');
    const [formData, setFormData] = useState({
        titre: '',
        description: '',
        dateEvent: '',
        lieu: '',
        capaciteTotal: '',
        prix: '',
        categoryId: '',
        imageUrl: ''
    });

    useEffect(() => {
        if (!user || user.role !== 'ORGANIZER') {
            navigate('/');
            return;
        }
        loadCategories();
    }, []);

    const loadCategories = async () => {
        try {
            const data = await getAllCategories();
            setCategories(data);
        } catch (error) {
            console.error('Erreur:', error);
        }
    };

    const handleChange = (e) => {
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage('');

        try {
            const eventData = {
                ...formData,
                capaciteTotal: parseInt(formData.capaciteTotal),
                capaciteDisponible: parseInt(formData.capaciteTotal),
                prix: parseFloat(formData.prix),
                organisateurId: user.id,
                category: { id: parseInt(formData.categoryId) }
            };

            await createEvent(eventData);
            setMessage('✅ Événement créé avec succès ! En attente d\'approbation admin.');
            setTimeout(() => {
                navigate('/organizer/my-events');
            }, 2000);
        } catch (error) {
            setMessage('❌ Erreur lors de la création de l\'événement');
            console.error(error);
        }
    };

    return (
        <div className="container">
            <div className="page-header">
                <div>
                    <h1>➕ Créer un Événement</h1>
                    <p>Proposez un nouvel événement (soumis à approbation admin)</p>
                </div>
            </div>

            <div className="event-details">
                {message && (
                    <div className={message.includes('✅') ? 'success-message' : 'error-message'}>
                        {message}
                    </div>
                )}
                
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>📝 Titre *</label>
                        <input
                            type="text"
                            name="titre"
                            value={formData.titre}
                            onChange={handleChange}
                            required
                            placeholder="Ex: Concert de Jazz à Casablanca"
                        />
                    </div>

                    <div className="form-group">
                        <label>📄 Description</label>
                        <textarea
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            rows="5"
                            placeholder="Décrivez votre événement..."
                        />
                    </div>

                    <div className="event-details-info">
                        <div className="form-group">
                            <label>📅 Date et Heure *</label>
                            <input
                                type="datetime-local"
                                name="dateEvent"
                                value={formData.dateEvent}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="form-group">
                            <label>📍 Lieu *</label>
                            <input
                                type="text"
                                name="lieu"
                                value={formData.lieu}
                                onChange={handleChange}
                                required
                                placeholder="Ex: Théâtre Mohammed V, Rabat"
                            />
                        </div>

                        <div className="form-group">
                            <label>👥 Capacité Totale *</label>
                            <input
                                type="number"
                                name="capaciteTotal"
                                value={formData.capaciteTotal}
                                onChange={handleChange}
                                min="1"
                                required
                                placeholder="Ex: 500"
                            />
                        </div>

                        <div className="form-group">
                            <label>💰 Prix (DH) *</label>
                            <input
                                type="number"
                                name="prix"
                                value={formData.prix}
                                onChange={handleChange}
                                min="0"
                                step="0.01"
                                required
                                placeholder="Ex: 250 (0 pour gratuit)"
                            />
                        </div>

                        <div className="form-group">
                            <label>📂 Catégorie *</label>
                            <select
                                name="categoryId"
                                value={formData.categoryId}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Sélectionner une catégorie</option>
                                {categories.map(cat => (
                                    <option key={cat.id} value={cat.id}>
                                        {cat.iconUrl} {cat.nom}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="form-group">
                            <label>🖼️ URL de l'image</label>
                            <input
                                type="url"
                                name="imageUrl"
                                value={formData.imageUrl}
                                onChange={handleChange}
                                placeholder="https://exemple.com/image.jpg"
                            />
                        </div>
                    </div>

                    <div style={{display: 'flex', gap: '1rem', marginTop: '2rem'}}>
                        <button type="submit" className="btn btn-success">
                            ✅ Créer l'événement
                        </button>
                        <button 
                            type="button" 
                            onClick={() => navigate('/organizer/my-events')} 
                            className="btn btn-secondary"
                        >
                            ❌ Annuler
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}

export default CreateEvent;