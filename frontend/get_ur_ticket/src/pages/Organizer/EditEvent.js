import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getEventById, updateEvent, getAllCategories } from '../../services/api';
import './CreateEvent.css';

const EditEvent = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [categories, setCategories] = useState([]);
    const user = JSON.parse(localStorage.getItem("user"));
const userId = user.id;
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
        loadEvent();
        loadCategories();
    }, [id]);

    const loadEvent = async () => {
        try {
            const event = await getEventById(id);
            setFormData({
                titre: event.titre,
                description: event.description,
                dateEvent: new Date(event.dateEvent).toISOString().slice(0, 16),
                lieu: event.lieu,
                capaciteTotal: event.capaciteTotal,
                prix: event.prix,
                categoryId: event.category?.id || '',
                imageUrl: event.imageUrl || ''
            });
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors du chargement de l\'événement');
        }
    };

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

   /* const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const eventData = {
                ...formData,
                dateEvent: new Date(formData.dateEvent).toISOString()
            };
            console.log(eventData);
            await updateEvent(id, eventData);

            alert('Événement modifié avec succès');
            navigate('/organizer/my-events');
        } catch (error) {
            console.error('Erreur:', error);
            alert('Erreur lors de la modification');
        }
    };
*/
const handleSubmit = async (e) => {
    e.preventDefault();

    try {
        const eventData = {
            titre: formData.titre,
            description: formData.description,
            dateEvent: new Date(formData.dateEvent).toISOString(),
            lieu: formData.lieu,
            capaciteTotal: formData.capaciteTotal,
            prix: formData.prix,
            imageUrl: formData.imageUrl,

            // 🔥 IMPORTANT
            organisateurId: userId, // ← mets l'ID réel de l'utilisateur connecté

            // 🔥 Structure attendue par Spring
            category: {
                id: formData.categoryId
            }
        };

        console.log(eventData);

        await updateEvent(id, eventData);

        alert('Événement modifié avec succès');
        navigate('/organizer/my-events');

    } catch (error) {
        console.error('Erreur:', error);
        alert('Erreur lors de la modification');
    }
};

    return (
        <div className="create-event-container">
            <h1>Modifier l'événement</h1>
            <form onSubmit={handleSubmit} className="event-form">
                <div className="form-group">
                    <label>Titre *</label>
                    <input
                        type="text"
                        name="titre"
                        value={formData.titre}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label>Description</label>
                    <textarea
                        name="description"
                        value={formData.description}
                        onChange={handleChange}
                        rows="4"
                    />
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Date et heure *</label>
                        <input
                            type="datetime-local"
                            name="dateEvent"
                            value={formData.dateEvent}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Catégorie *</label>
                        <select
                            name="categoryId"
                            value={formData.categoryId}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Sélectionner...</option>
                            {categories.map(cat => (
                                <option key={cat.id} value={cat.id}>
                                    {cat.iconUrl} {cat.nom}
                                </option>
                            ))}
                        </select>
                    </div>
                </div>

                <div className="form-group">
                    <label>Lieu *</label>
                    <input
                        type="text"
                        name="lieu"
                        value={formData.lieu}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label>Capacité totale *</label>
                        <input
                            type="number"
                            name="capaciteTotal"
                            value={formData.capaciteTotal}
                            onChange={handleChange}
                            min="1"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Prix (DH) *</label>
                        <input
                            type="number"
                            name="prix"
                            value={formData.prix}
                            onChange={handleChange}
                            min="0"
                            step="0.01"
                            required
                        />
                    </div>
                </div>

                <div className="form-group">
                    <label>URL de l'image</label>
                    <input
                        type="url"
                        name="imageUrl"
                        value={formData.imageUrl}
                        onChange={handleChange}
                        placeholder="https://example.com/image.jpg"
                    />
                </div>

                <div className="form-actions">
                    <button type="button" className="btn btn-secondary" onClick={() => navigate('/organizer/my-events')}>
                        Annuler
                    </button>
                    <button type="submit" className="btn btn-primary">
                        Enregistrer les modifications
                    </button>
                </div>
            </form>
        </div>
    );
};

export default EditEvent;