// src/hooks/useSound.js

const useSound = () => {

    // ✅ Son généré en JavaScript - pas besoin de fichier MP3 !
    const playNotificationSound = () => {
        try {
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();

            // Créer un son de notification agréable (deux notes)
            const playNote = (frequency, startTime, duration, volume = 0.3) => {
                const oscillator = audioContext.createOscillator();
                const gainNode   = audioContext.createGain();

                oscillator.connect(gainNode);
                gainNode.connect(audioContext.destination);

                oscillator.type      = 'sine';
                oscillator.frequency.setValueAtTime(frequency, startTime);

                // Envelope: fade in + fade out
                gainNode.gain.setValueAtTime(0, startTime);
                gainNode.gain.linearRampToValueAtTime(volume, startTime + 0.05);
                gainNode.gain.linearRampToValueAtTime(0, startTime + duration);

                oscillator.start(startTime);
                oscillator.stop(startTime + duration);
            };

            const now = audioContext.currentTime;

            // ✅ Son de notification : 3 notes montantes
            playNote(523, now,        0.15); // Do
            playNote(659, now + 0.15, 0.15); // Mi
            playNote(784, now + 0.30, 0.25); // Sol

        } catch (e) {
            console.warn('Son non disponible:', e);
        }
    };

    const playErrorSound = () => {
        try {
            const audioContext = new (window.AudioContext || window.webkitAudioContext)();
            const now = audioContext.currentTime;

            const playNote = (frequency, startTime, duration) => {
                const oscillator = audioContext.createOscillator();
                const gainNode   = audioContext.createGain();
                oscillator.connect(gainNode);
                gainNode.connect(audioContext.destination);
                oscillator.type = 'sine';
                oscillator.frequency.setValueAtTime(frequency, startTime);
                gainNode.gain.setValueAtTime(0.2, startTime);
                gainNode.gain.linearRampToValueAtTime(0, startTime + duration);
                oscillator.start(startTime);
                oscillator.stop(startTime + duration);
            };

            // Son d'annulation : 2 notes descendantes
            playNote(440, now,        0.2);
            playNote(330, now + 0.2,  0.3);

        } catch (e) {
            console.warn('Son non disponible:', e);
        }
    };

    return { playNotificationSound, playErrorSound };
};

export default useSound;