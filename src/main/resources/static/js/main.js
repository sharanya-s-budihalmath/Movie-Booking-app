// ===================== TOAST NOTIFICATION SYSTEM =====================
function showToast(message, type = 'success') {
    let container = document.getElementById('toast-container');
    if (!container) {
        container = document.createElement('div');
        container.id = 'toast-container';
        document.body.appendChild(container);
    }

    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-exclamation-circle'}"></i>
        <span>${message}</span>
    `;
    container.appendChild(toast);

    setTimeout(() => {
        toast.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
        toast.style.opacity = '0';
        toast.style.transform = 'translateX(50px)';
        setTimeout(() => toast.remove(), 400);
    }, 3500);
}

// ===================== HERO CAROUSEL =====================
let currentSlide = 0;
let carouselTimer = null;

function initHeroCarousel() {
    const slides = document.querySelectorAll('.hero-slide');
    if (slides.length <= 1) return;

    startCarouselTimer();
}

function startCarouselTimer() {
    if (carouselTimer) clearInterval(carouselTimer);
    carouselTimer = setInterval(() => {
        nextHeroSlide();
    }, 7000);
}

function showHeroSlide(index) {
    const slides = document.querySelectorAll('.hero-slide');
    if (!slides || slides.length === 0) return;
    slides.forEach(s => s.classList.remove('active'));
    currentSlide = (index + slides.length) % slides.length;
    slides[currentSlide].classList.add('active');
}

function nextHeroSlide() {
    const slides = document.querySelectorAll('.hero-slide');
    if (slides.length > 0) {
        showHeroSlide(currentSlide + 1);
        startCarouselTimer();
    }
}

function prevHeroSlide() {
    const slides = document.querySelectorAll('.hero-slide');
    if (slides.length > 0) {
        showHeroSlide(currentSlide - 1);
        startCarouselTimer();
    }
}

function copyPromoCode(code) {
    navigator.clipboard.writeText(code).then(() => {
        showToast(`Promo code '${code}' copied to clipboard! Use at checkout for 50% OFF 🎉`, 'success');
    }).catch(() => {
        showToast(`Promo code is ${code}! Apply at checkout for 50% OFF`, 'success');
    });
}

function selectBank(element) {
    document.querySelectorAll('.bank-btn').forEach(b => b.classList.remove('active'));
    if (element) {
        element.classList.add('active');
        showToast('Selected bank: ' + element.innerText.trim(), 'success');
    }
}

// ===================== TRAILER MODAL =====================
function openTrailerModal(embedUrl) {
    let modal = document.getElementById('trailerModal');
    if (!modal) {
        modal = document.createElement('div');
        modal.id = 'trailerModal';
        modal.className = 'modal-overlay';
        modal.innerHTML = `
            <div class="modal-content-video">
                <span class="modal-close-btn" onclick="closeTrailerModal()">&times;</span>
                <iframe id="trailerIframe" width="100%" height="100%" src="" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
            </div>
        `;
        document.body.appendChild(modal);
    }
    const iframe = document.getElementById('trailerIframe');
    if (iframe && embedUrl) {
        const cleanUrl = embedUrl.split('?')[0];
        iframe.src = cleanUrl + "?autoplay=1&rel=0&enablejsapi=1";
    }
    modal.classList.add('active');
}

function closeTrailerModal() {
    const modal = document.getElementById('trailerModal');
    if (modal) {
        modal.classList.remove('active');
        const iframe = document.getElementById('trailerIframe');
        if (iframe) iframe.src = "";
    }
}

// Close modal on escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeTrailerModal();
});

// ===================== SEAT SELECTION =====================
let selectedSeats = [];

function toggleSeat(el) {
    const seatNumber = el.getAttribute('data-seat');
    const isBooked = el.getAttribute('data-booked') === 'true';

    if (isBooked) {
        showToast('This seat is already booked.', 'error');
        return;
    }

    if (el.classList.contains('selected')) {
        el.classList.remove('selected');
        selectedSeats = selectedSeats.filter(s => s !== seatNumber);
        showToast(`Deselected seat ${seatNumber}`, 'success');
    } else {
        if (selectedSeats.length >= 10) {
            showToast('Maximum 10 seats allowed per booking.', 'error');
            return;
        }
        el.classList.add('selected');
        selectedSeats.push(seatNumber);
        showToast(`Selected seat ${seatNumber}`, 'success');
    }

    updateSummary();
}

function updateSummary() {
    const listEl = document.getElementById('selectedSeatsList');
    const countEl = document.getElementById('seatCount');
    const totalEl = document.getElementById('totalAmount');
    const proceedBtn = document.getElementById('proceedBtn');
    const inputEl = document.getElementById('selectedSeatsInput');

    if (inputEl) inputEl.value = selectedSeats.join(',');

    if (!listEl) return;

    if (selectedSeats.length === 0) {
        listEl.textContent = 'None';
        countEl.textContent = '0';
        totalEl.textContent = '0.00';
        if (proceedBtn) proceedBtn.disabled = true;
    } else {
        listEl.textContent = selectedSeats.join(', ');
        countEl.textContent = selectedSeats.length;
        const total = selectedSeats.length * (typeof ticketPrice !== 'undefined' ? ticketPrice : 0);
        totalEl.textContent = total.toFixed(2);
        if (proceedBtn) proceedBtn.disabled = false;
    }
}

function prepareBooking() {
    const input = document.getElementById('selectedSeatsInput');
    if (input) input.value = selectedSeats.join(',');
    return true;
}

// ===================== PAYMENT INTERACTIVE UI =====================
function formatCardNumber(input) {
    let val = input.value.replace(/\D/g, '').substring(0, 16);
    let formatted = val.match(/.{1,4}/g)?.join(' ') || val;
    input.value = formatted;
    
    const display = document.getElementById('cardDisplayNumber');
    if (display) {
        display.textContent = formatted.padEnd(19, '•');
    }
}

function formatCardHolder(input) {
    const display = document.getElementById('cardDisplayHolder');
    if (display) {
        display.textContent = input.value.toUpperCase() || 'CARDHOLDER NAME';
    }
}

function switchPayTab(tabName) {
    const tabs = document.querySelectorAll('.pay-tab');
    tabs.forEach(t => t.classList.remove('active'));
    
    const targetTab = document.querySelector(`.pay-tab[data-tab="${tabName}"]`);
    if (targetTab) targetTab.classList.add('active');

    const sections = ['cardSection', 'upiSection', 'netbankingSection'];
    sections.forEach(sec => {
        const el = document.getElementById(sec);
        if (el) el.style.display = (sec === tabName + 'Section') ? 'block' : 'none';
    });
}

function simulatePayment() {
    return confirm('Proceed to confirm your booking payment?');
}

// ===================== PROMO CODE DISCOUNT SYSTEM =====================
let isPromoApplied = false;

function applyPromoCode() {
    const input = document.getElementById('promoInput');
    const msgEl = document.getElementById('promoMsg');
    const discountRow = document.getElementById('discountRow');
    const discountAmount = document.getElementById('discountAmount');
    const finalTotalDisplay = document.getElementById('finalTotalDisplay');
    const btnPayAmount = document.getElementById('btnPayAmount');

    if (!input || !msgEl) return;
    const code = input.value.trim().toUpperCase();

    if (isPromoApplied) {
        msgEl.innerHTML = '<span style="color: var(--accent-gold);"><i class="fas fa-info-circle"></i> Promo code CINE50 is already active on this booking!</span>';
        return;
    }

    if (code === 'CINE50') {
        isPromoApplied = true;
        const discountVal = typeof baseTotal !== 'undefined' ? (baseTotal * 0.5) : 0;
        const newTotal = typeof baseTotal !== 'undefined' ? (baseTotal * 0.5) : 0;

        if (discountRow) discountRow.style.display = 'flex';
        if (discountAmount) discountAmount.textContent = discountVal.toFixed(2);
        if (finalTotalDisplay) finalTotalDisplay.textContent = newTotal.toFixed(2);
        if (btnPayAmount) btnPayAmount.textContent = newTotal.toFixed(2);

        msgEl.innerHTML = '<span style="color: #10b981; font-weight: 700;"><i class="fas fa-check-circle"></i> Code CINE50 Applied! You saved 50% (₹' + discountVal.toFixed(2) + ')! 🎉</span>';
        showToast('Promo Code CINE50 applied successfully! Saved 50%', 'success');
    } else if (code === '') {
        msgEl.innerHTML = '<span style="color: var(--primary);"><i class="fas fa-exclamation-circle"></i> Please enter a valid coupon code.</span>';
    } else {
        msgEl.innerHTML = '<span style="color: var(--primary);"><i class="fas fa-times-circle"></i> Invalid coupon code. Try CINE50 for 50% discount!</span>';
    }
}


// ===================== DATE FILTER TABS (DETAIL PAGE) =====================
function filterShowsByDate(dateStr, btnEl) {
    document.querySelectorAll('.date-tab-btn').forEach(b => b.classList.remove('active'));
    if (btnEl) btnEl.classList.add('active');

    document.querySelectorAll('.show-card-modern').forEach(card => {
        const cardDate = card.getAttribute('data-date');
        if (dateStr === 'all' || cardDate === dateStr) {
            card.style.display = 'block';
        } else {
            card.style.display = 'none';
        }
    });
}

// ===================== 3D DOTTED SURFACE BACKGROUND =====================
function initDottedSurface() {
    if (document.getElementById('dotted-surface-canvas')) return;

    const container = document.createElement('div');
    container.id = 'dotted-surface-canvas';
    document.body.prepend(container);

    const loadThree = (callback) => {
        if (window.THREE) {
            callback();
        } else {
            const script = document.createElement('script');
            script.src = 'https://cdnjs.cloudflare.com/ajax/libs/three.js/r128/three.min.js';
            script.onload = callback;
            document.head.appendChild(script);
        }
    };

    loadThree(() => {
        const THREE = window.THREE;
        const SEPARATION = 140;
        const AMOUNTX = 45;
        const AMOUNTY = 55;

        const scene = new THREE.Scene();
        scene.fog = new THREE.FogExp2(0x0b0e14, 0.0006);

        const camera = new THREE.PerspectiveCamera(
            60,
            window.innerWidth / window.innerHeight,
            1,
            10000
        );
        camera.position.set(0, 325, 1100);

        const renderer = new THREE.WebGLRenderer({ alpha: true, antialias: true });
        renderer.setPixelRatio(window.devicePixelRatio || 1);
        renderer.setSize(window.innerWidth, window.innerHeight);
        renderer.setClearColor(0x0b0e14, 0);

        container.appendChild(renderer.domElement);

        const positions = [];
        const colors = [];
        const geometry = new THREE.BufferGeometry();

        for (let ix = 0; ix < AMOUNTX; ix++) {
            for (let iy = 0; iy < AMOUNTY; iy++) {
                const x = ix * SEPARATION - (AMOUNTX * SEPARATION) / 2;
                const y = 0;
                const z = iy * SEPARATION - (AMOUNTY * SEPARATION) / 2;

                positions.push(x, y, z);
                
                // Futuristic glowing red & cyan dotted theme
                if ((ix + iy) % 5 === 0) {
                    colors.push(0.9, 0.04, 0.08); // CineBook Red (#e50914)
                } else if ((ix + iy) % 3 === 0) {
                    colors.push(0.0, 0.95, 1.0);  // Cyan Glow (#00f2fe)
                } else {
                    colors.push(0.7, 0.75, 0.85); // Silver/White
                }
            }
        }

        geometry.setAttribute('position', new THREE.Float32BufferAttribute(positions, 3));
        geometry.setAttribute('color', new THREE.Float32BufferAttribute(colors, 3));

        const material = new THREE.PointsMaterial({
            size: 6.5,
            vertexColors: true,
            transparent: true,
            opacity: 0.8,
            sizeAttenuation: true,
        });

        const points = new THREE.Points(geometry, material);
        scene.add(points);

        let count = 0;

        const animate = () => {
            requestAnimationFrame(animate);

            const positionAttribute = geometry.attributes.position;
            const posArray = positionAttribute.array;

            let i = 0;
            for (let ix = 0; ix < AMOUNTX; ix++) {
                for (let iy = 0; iy < AMOUNTY; iy++) {
                    const index = i * 3;
                    posArray[index + 1] =
                        Math.sin((ix + count) * 0.3) * 50 +
                        Math.sin((iy + count) * 0.5) * 50;
                    i++;
                }
            }

            positionAttribute.needsUpdate = true;
            renderer.render(scene, camera);
            count += 0.07;
        };

        const handleResize = () => {
            camera.aspect = window.innerWidth / window.innerHeight;
            camera.updateProjectionMatrix();
            renderer.setSize(window.innerWidth, window.innerHeight);
        };

        window.addEventListener('resize', handleResize);
        animate();
    });
}

// ===================== CINEMA AUTH & TRIVIA HELPERS =====================
function quickFillLogin(email, password) {
    const emailInput = document.getElementById('loginEmail');
    const passwordInput = document.getElementById('loginPassword');
    const form = document.getElementById('loginForm');

    if (emailInput && passwordInput) {
        emailInput.value = email;
        passwordInput.value = password;

        if (typeof showToast === 'function') {
            showToast(`Filled credentials for ${email}! Logging in...`, 'success');
        }

        setTimeout(() => {
            if (form) form.submit();
        }, 400);
    }
}

function togglePasswordVisibility(inputId, btnEl) {
    const input = document.getElementById(inputId);
    if (!input) return;

    const icon = btnEl.querySelector('i');
    if (input.type === 'password') {
        input.type = 'text';
        if (icon) icon.className = 'fas fa-eye-slash';
    } else {
        input.type = 'password';
        if (icon) icon.className = 'fas fa-eye';
    }
}

const cinemaTriviaFacts = [
    "🍿 Movie theater popcorn was originally banned in silent film cinemas in 1890 because it made too much crunching noise!",
    "🎬 Over 500 gallons of fake blood were specially manufactured for the epic battle scenes in Kill Bill!",
    "🦖 The sound of T-Rex footsteps in Jurassic Park was created by hitting a guitar string attached to a pineapple!",
    "🚀 Timothée Chalamet spent months practicing Sandworm riding and desert choreography for Dune: Part Two!",
    "🏎️ The iconic DeLorean time machine in Back to the Future was originally scripted as a household refrigerator!",
    "🧙 Yoda's face design was inspired by Albert Einstein to instantly communicate wisdom and intelligence!",
    "🍿 Popcorn became a cinema staple during the Great Depression because it was delicious and cost only 5 cents!"
];

let currentTriviaIndex = 0;

function nextCinemaTrivia() {
    currentTriviaIndex = (currentTriviaIndex + 1) % cinemaTriviaFacts.length;
    const textEl = document.getElementById('triviaText');
    const idxEl = document.getElementById('triviaIndex');
    if (textEl) {
        textEl.style.opacity = '0';
        textEl.style.transform = 'translateY(10px)';
        setTimeout(() => {
            textEl.textContent = cinemaTriviaFacts[currentTriviaIndex];
            if (idxEl) idxEl.textContent = currentTriviaIndex + 1;
            textEl.style.transition = 'opacity 0.4s ease, transform 0.4s ease';
            textEl.style.opacity = '1';
            textEl.style.transform = 'translateY(0)';
        }, 200);
    }
}

// ===================== INIT ON DOM LOADED =====================
document.addEventListener('DOMContentLoaded', () => {
    initDottedSurface();
    initHeroCarousel();

    // Auto dismiss existing alert boxes
    setTimeout(() => {
        document.querySelectorAll('.alert').forEach(a => {
            a.style.transition = 'opacity 0.5s';
            a.style.opacity = '0';
            setTimeout(() => a.remove(), 500);
        });
    }, 4000);
});

